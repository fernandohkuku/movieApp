package com.fernando.movieapp.common.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.fernando.core.domain.entities.NotificationEntity
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.fernando.movieapp.domain.usecases.DisplayNotificationUseCase
import com.fernando.movieapp.domain.usecases.GetLocationEveryUseCase
import com.fernando.movieapp.domain.usecases.SaveLocationUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
internal class LocationService : Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Main + serviceJob)

    @Inject
    lateinit var displayNotificationUseCase: DisplayNotificationUseCase

    @Inject
    lateinit var getLocationEveryUseCase: GetLocationEveryUseCase

    @Inject
    lateinit var saveLocationUseCase: SaveLocationUseCase

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        getLocationsUpdates { location ->
            sendLocation(location)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun displayNotification() = serviceScope.launch(Main) {
        val notification =
            NotificationEntity(
                "Ubicación guardada",
                "Hemos almacenado su ubicación",
                "https://i.pinimg.com/originals/4a/0e/1f/4a0e1f8fbec8d2108e0434fc48ef88f3.jpg"
            )
        val params = DisplayNotificationUseCase.Params(notification, mapOf())
        displayNotificationUseCase(params).fold({
            Timber.i("Notification displayed")
        }, { error ->
            Timber.e(error)
        })
    }

    private fun getLocationsUpdates(onLocation: (location: LocationEntity) -> Unit) =
        serviceScope.launch(Main) {
            getLocationEveryUseCase().subscribe({ location ->
                location.map { locationEntity ->
                    onLocation(locationEntity)
                }.collect()
            })
        }

    private fun sendLocation(location: LocationEntity) = serviceScope.launch(IO) {
        val body = LocationUserEntity(
            location.latitude,
            location.longitude,
            Calendar.getInstance().timeInMillis
        )
        saveLocationUseCase(body).fold({
            displayNotification()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

}