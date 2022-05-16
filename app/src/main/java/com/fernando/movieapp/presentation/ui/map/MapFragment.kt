package com.fernando.movieapp.presentation.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.fernando.movieapp.R
import com.fernando.movieapp.databinding.FragmentMapBinding
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.fernando.ui_ktx.content.getMapFragment
import com.fernando.ui_ktx.google.addMarker
import com.fernando.ui_ktx.google.config
import com.fernando.ui_ktx.google.moveCamera
import com.fernando.ui_ktx.google.show
import com.fernando.ui_ktx.kotlin.isNotNull
import com.fernando.ui_ktx.lifecycle.observeNewData
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {
    private val binding: FragmentMapBinding by dataBinding()
    private val viewModel: MapViewModel by viewModels()


    private val Fragment.mapFragment: SupportMapFragment
        get() = getMapFragment(R.id.mapView)

    private val googleMap: GoogleMap?
        get() = viewModel.liveGoogleMap.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupMapView()
    }

    private fun startLocations()= with(viewModel){
        getLocation()
        getLocations()
    }

    private fun setupObservers() = with(viewModel) {
        liveGoogleMap.observe(viewLifecycleOwner, ::setupGoogleMap)
        liveLocations.observeNewData(viewLifecycleOwner, ::setMarkers)
        liveCurrentLocation.observe(viewLifecycleOwner, ::setupUserLocation)
    }


    private fun setupMapView() = with(viewModel) {
        createMap(mapFragment)
    }

    private fun setupGoogleMap(googleMap: GoogleMap?) {
        if (googleMap.isNotNull()) {
            startLocations()
            googleMap.config(
                onClickMarker = { marker ->
                    marker.setOnclick()
                }
            )
        }
    }

    private fun setMarkers(locations: List<LocationUserEntity>) {
        locations.forEach { location ->
            googleMap?.addMarker(location.latLng, location.dateAsString, location)
        }
    }

    private fun setupUserLocation(locationEntity: LocationEntity) {
        googleMap?.moveCamera(locationEntity.latLng)
    }

    private fun Marker.setOnclick() {
        show()
    }
}