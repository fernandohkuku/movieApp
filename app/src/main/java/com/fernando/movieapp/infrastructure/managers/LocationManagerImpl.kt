package com.fernando.movieapp.infrastructure.managers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.managers.LocationManager
import com.fernando.movieapp.infrastructure.exceptions.PermissionException
import com.fernando.movieapp.infrastructure.mappers.toEntity
import com.fernando.movieapp.infrastructure.mappers.toLocationException
import com.fernando.ui_ktx.content.hasPermission
import com.fernando.ui_ktx.kotlin.isNotNull
import com.fernando.ui_ktx.kotlin.isNull
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.minutes

@SuppressLint("MissingPermission")
internal class LocationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationProvider: FusedLocationProviderClient
) : LocationManager {
    companion object {
        private const val TIMEOUT_MILLIS = 20 * 1000
    }

    private object Permission {
        const val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    private object Threshold {
        const val ANTIQUITY_MILLIS = 15 * 60 * 1000
        const val TIME_DIFFERENCE_MILLIS = 5 * 60 * 1000
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 0
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }
//    300000L
    private val locationRequestEvery = LocationRequest.create().apply {
        interval = 300000L
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    private lateinit var locationCallback: LocationCallback

    override suspend fun requestEnable() {
        val settingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        try {
            LocationServices.getSettingsClient(context)
                .checkLocationSettings(settingRequest)
                .await()
        } catch (ex: ApiException) {
            throw ex.toLocationException()
        }
    }

    override suspend fun getLatestLocation(): LocationEntity = validate().run {
        val lastLocationDto = locationProvider.lastLocation.await()
        if (lastLocationDto.hasRequiredAntiquity()) {
            return lastLocationDto.toEntity()
        }

        return getBetterLocation(lastLocationDto)
    }

    override suspend fun getLocationEveryTime(): Flow<LocationEntity> = validate().run {
        val lastLocationDto = locationProvider.lastLocation.await()
        return getLocationEverys(lastLocationDto)
    }

    private fun validate() {
        if (!context.hasPermission(Permission.LOCATION)) {
            throw PermissionException("Location permission required")
        }
    }

    private fun Location?.hasRequiredAntiquity(): Boolean {
        if (isNull()) {
            return false
        }

        val currentTimeMills = System.currentTimeMillis()
        return currentTimeMills - this!!.time < Threshold.ANTIQUITY_MILLIS
    }

    private suspend fun getBetterLocation(lastLocation: Location?): LocationEntity =
        suspendCoroutine { continuation ->
            val requestTimeMillis = System.currentTimeMillis()
            startLocationUpdates { locationResult ->
                var locationDto = locationResult.getBetterLocationThan(lastLocation)

                if (locationDto.isNull() && hasTimeoutPassed(requestTimeMillis)) {
                    locationDto = lastLocation
                }

                if (locationDto.isNotNull()) {
                    stopLocationUpdates()
                    continuation.resume(locationDto.toEntity())
                    return@startLocationUpdates
                }
            }
        }

    private suspend fun getLocationEvery(lastLocation: Location?): LocationEntity =
        suspendCoroutine { continuation ->
            val requestTimeMillis = System.currentTimeMillis()
            startLocationUpdate { locationResult ->
                var locationDto = locationResult.getBetterLocationThan(lastLocation)
                if (locationDto.isNull() && hasTimeoutPassed(requestTimeMillis)) {
                    locationDto = lastLocation
                }
                if (locationDto.isNotNull()) {
                    continuation.resume(locationDto.toEntity())
                    return@startLocationUpdate
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getLocationEverys(lastLocation: Location?): Flow<LocationEntity> =
        callbackFlow {
            val requestTimeMillis = System.currentTimeMillis()
            startLocationUpdate { locationResult ->
                var locationDto = locationResult.getBetterLocationThan(lastLocation)
                if (locationDto.isNull() && hasTimeoutPassed(requestTimeMillis)) {
                    locationDto = lastLocation
                }
                if (locationDto.isNotNull()) {
                    trySend(locationDto.toEntity())
                }
            }
            awaitClose { stopLocationUpdates() }
        }

    private fun startLocationUpdates(callback: (LocationResult) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                callback(locationResult)
            }
        }
        locationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun startLocationUpdate(callback: (LocationResult) -> Unit) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                callback(locationResult)
            }
        }
        locationProvider.requestLocationUpdates(
            locationRequestEvery,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun LocationResult.getBetterLocationThan(lastLocation: Location?): Location? {
        for (location in locations) {
            if (location.isBetterLocation(lastLocation)) {
                return location
            }
        }

        return null
    }

    private fun Location.isBetterLocation(oldLocationDto: Location?): Boolean {
        if (oldLocationDto.isNull()) return true

        val timeDeltaMillis = time - oldLocationDto!!.time
        val isSignificantlyNewer = timeDeltaMillis > Threshold.TIME_DIFFERENCE_MILLIS
        val isSignificantlyOlder = timeDeltaMillis < -Threshold.TIME_DIFFERENCE_MILLIS
        val isNewer = timeDeltaMillis > 0

        if (isSignificantlyNewer)
            return true
        else if (isSignificantlyOlder)
            return false

        val accuracyDelta = (accuracy - oldLocationDto.accuracy).toInt()
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200

        return if (isMoreAccurate) true
        else if (isNewer && !isLessAccurate) true
        else isNewer && !isSignificantlyLessAccurate
    }

    private fun hasTimeoutPassed(requestTimeMillis: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return requestTimeMillis - currentTimeMillis > TIMEOUT_MILLIS
    }

    private fun stopLocationUpdates() = locationProvider.removeLocationUpdates(locationCallback)
}