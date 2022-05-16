package com.fernando.movieapp.data.remote


import com.fernando.movieapp.data.models.LocationUserDto
import com.fernando.movieapp.data.repositories.LocationRemoteDataSource
import com.fernando.ui_ktx.firebase.getAsyncResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(): LocationRemoteDataSource {
    private object Collection {
        const val LOCATIONS = "locations"
    }
    private val db: FirebaseFirestore
        get() = Firebase.firestore


    private val locationCollection: CollectionReference
        get() = db.collection(Collection.LOCATIONS)

    override suspend fun saveLocation(locationUser:LocationUserDto) {
        sendLocation(locationUser)
    }

    override suspend fun getLocations(): List<LocationUserDto> {
        val result = locationCollection.getAsyncResult()
        return result.toObjects(LocationUserDto::class.java)
    }

    private suspend fun sendLocation(locationUser: LocationUserDto){
        locationCollection.document().set(locationUser).await()
    }
}