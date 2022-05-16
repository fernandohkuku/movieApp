package com.fernando.movieapp.presentation.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.fernando.movieapp.domain.usecases.GetLocationUseCase
import com.fernando.movieapp.domain.usecases.GetLocationsUseCase
import com.fernando.ui_ktx.content.createMap
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
internal class MapViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val getLocationsUseCase: GetLocationsUseCase
) : ViewModel() {

    private var lastLocation: LocationEntity? = null


    private val mLiveGoogleMap = MutableLiveData<GoogleMap?>()
    val liveGoogleMap = mLiveGoogleMap

    private val mLiveCurrentLocation = MutableLiveData<LocationEntity>()
    val liveCurrentLocation = mLiveCurrentLocation


    private val mLiveLocations = MutableLiveData<List<LocationUserEntity>>()
    val liveLocations = mLiveLocations


    fun createMap(mapFragment: SupportMapFragment) = viewModelScope.launch(Dispatchers.Main) {
        mLiveGoogleMap.value = mapFragment.createMap()
    }

    fun getLocation() = viewModelScope.launch(Main) {
        getLocationUseCase().subscribe(::onSuccessLocation) { error ->
            onError(error)
        }
    }

    fun getLocations() = viewModelScope.launch(Main) {
        getLocationsUseCase().fold({ locations ->
            mLiveLocations.setValue(locations)
        }, ::onError)
    }

    private fun setCurrentLocation(locationEntity: LocationEntity) {
        lastLocation = locationEntity
        mLiveCurrentLocation.value = locationEntity
    }

    private suspend fun onSuccessLocation(locationEntity: LocationEntity) {
        setCurrentLocation(locationEntity)
    }

    private fun onError(exception: Exception) {
        Timber.e(exception)
    }


}