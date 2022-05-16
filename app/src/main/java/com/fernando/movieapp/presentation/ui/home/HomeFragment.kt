package com.fernando.movieapp.presentation.ui.home

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.fernando.core.domain.entities.MovieEntity
import com.fernando.core.presentation.components.permissions.PermissionLifecycle
import com.fernando.core.presentation.components.permissions.PermissionRequester
import com.fernando.core.presentation.ui.loader.AdapterLoadState
import com.fernando.movieapp.R
import com.fernando.movieapp.databinding.FragmentHomeBinding
import com.fernando.movieapp.infrastructure.exceptions.LocationException
import com.fernando.ui_ktx.content.appSettings
import com.fernando.ui_ktx.content.navigate
import com.fernando.ui_ktx.kotlin.isNotNull
import com.fernando.ui_ktx.widget.*
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), OnSnapPositionChangeListener {

    private object Permission {
        const val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val binding: FragmentHomeBinding by dataBinding()
    private val viewModel: HomeViewModel by viewModels()
    private val movieAdapter by lazy { MovieAdapter() }


    private val locationPermission = PermissionLifecycle(Permission.LOCATION)

    private val locationPermissionRequester =
        PermissionRequester(this, Permission.LOCATION, onDenied = {
            appSettings(requireContext().packageName)
        })

    private val locationResolutionContract = ActivityResultContracts.StartIntentSenderForResult()

    private val locationResolutionContent =
        registerForActivityResult(locationResolutionContract) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadLocationsUpdates()
            }
        }

    private val storagePermission = PermissionRequester(this, Permission.STORAGE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationPermission.attach(this)
        setupObservers()
        prepareUI()
        binding.rvMovies.setupMovies()
    }

    private fun prepareUI() = with(binding) {
        prepareLocationRequest()
        fbMap.setOnClickListener {
            locationPermissionRequester.runWithPermission {
                val action = HomeFragmentDirections.actionHomeToMap()
                navigate(action)
            }
        }
        fbGallery.setOnClickListener {
            navigateToGalery()
        }
    }


    private fun navigateToGalery() = storagePermission.runWithPermission {
        val action = HomeFragmentDirections.actionHomeToGalery()
        navigate(action)
    }

    private fun prepareLocationRequest() = with(binding.lLocationPermission) {
        btnRequestLocation.setOnClickListener {
            locationPermissionRequester.runWithPermission(onGranted = locationPermission::granted)
        }
    }

    private fun RecyclerView.setupMovies() {
        adapter = movieAdapter.withLoadStateFooter(
            footer = AdapterLoadState { movieAdapter.retry() }
        )
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        movieAdapter.addLoadStateListener { state ->
            state.badConnection()
        }
        snapHelper()
    }

    private fun snapHelper() = with(binding) {
        val snapHelper = LinearSnapHelper()
        rvMovies.attachSnapHelperWithListener(
            snapHelper,
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            this@HomeFragment
        )
    }

    private fun CombinedLoadStates.badConnection() = with(viewModel) {
        if (refresh is LoadState.Error) {
            localMovies.observe(viewLifecycleOwner, ::onLocalSuccess)
        }
    }

    private fun setupObservers() = with(viewModel) {
        locationPermission.observe(::onPermissionChange)
        remoteMovies.observe(viewLifecycleOwner, ::onRemoteSuccess)
        locationError.observe(viewLifecycleOwner, ::onLocationError)
    }

    private fun HomeViewModel.loadLocationsUpdates() {
//        liveLocation.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "${it.latitude}", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun onPermissionChange(hasPermission: Boolean) = with(binding) {
        lLocationPermission.root.isGone = hasPermission
        this.hasPermission = hasPermission
    }


    private fun onRemoteSuccess(movies: PagingData<MovieEntity>) {
        movieAdapter.submitData(lifecycle, movies)
    }

    private fun onLocalSuccess(movies: PagingData<MovieEntity>) {
        movieAdapter.submitData(lifecycle, movies)
    }

    private fun onLocationError(exception: LocationException) {
        if (exception.resolution.isNotNull()) {
            val intent = IntentSenderRequest.Builder(exception.resolution).build()
            locationResolutionContent.launch(intent)
        }
    }

    override fun onSnapPositionChange(position: Int) = with(binding) {
        val movie = movieAdapter.getMovie(position)
        ivBackdrop.setImageURI(movie?.backdrop)
        tvRating.text = (movie?.vote_average ?: "").toString()
    }
}