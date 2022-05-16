package com.fernando.movieapp.presentation.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.fernando.core.data.exceptions.NotInternetException
import com.fernando.core.domain.entities.NotificationEntity
import com.fernando.core.domain.usecases.GetLocalMoviesUseCase
import com.fernando.core.domain.usecases.GetRemoteMoviesUseCase
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.usecases.DisplayNotificationUseCase
import com.fernando.movieapp.domain.usecases.GetLocationEveryUseCase
import com.fernando.movieapp.domain.usecases.GetLocationUseCase
import com.fernando.movieapp.infrastructure.exceptions.LocationException
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getRemoteMoviesUseCase: GetRemoteMoviesUseCase,
    private val getLocalMoviesUseCase: GetLocalMoviesUseCase,
    private val getLocationEveryUseCase: GetLocationEveryUseCase,
    private val displayNotificationUseCase: DisplayNotificationUseCase
) : ViewModel() {

    private val mError = MutableLiveData<Boolean>()
    val liveError = mError

    private val mIsLoading = MutableLiveData(true)
    val isLoading = mIsLoading


    private val mLocation = MutableLiveData<LocationEntity>()
    val location get() = mLocation

    private val mLocationError = MutableLiveData<LocationException>()
    val locationError: LiveData<LocationException> = mLocationError

    val liveLocation = liveData(IO) {
        getLocationEveryUseCase().subscribe({ location ->
            location.collect { emit(it) }
        }, { error ->
            if (error is LocationException) {
                mLocationError.value = error
            }
            onError(error)
        })
    }

    val remoteMovies = liveData(IO) {
        getRemoteMoviesUseCase(viewModelScope).subscribe({ movies ->
            movies.collect {
                emit(it)
            }
        }, ::onError)
    }

    val localMovies = liveData(IO) {
        getLocalMoviesUseCase(viewModelScope).subscribe({ movies ->
            movies.collect {
                emit(it)
            }
            stopLoading()
        }, ::onError)
    }


    fun displayNotification() = viewModelScope.launch(Main) {
        val notification = NotificationEntity("test", "test", "test")
        val params = DisplayNotificationUseCase.Params(notification, mapOf())

        displayNotificationUseCase(params).fold({
            Timber.i("Notification displayed")
        }, { error ->
            Timber.e(error)
        })
    }

    private fun onError(exception: Exception) {
        stopLoading()
        Timber.e(exception)
    }

    private fun startLoading() {
        mIsLoading.postValue(true)
    }

    private fun stopLoading() = viewModelScope.launch(Main) {
        mIsLoading.value = false
    }

}