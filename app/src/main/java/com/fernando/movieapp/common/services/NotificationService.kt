package com.fernando.movieapp.common.services

import com.fernando.core.domain.entities.NotificationEntity
import com.fernando.movieapp.domain.usecases.DisplayNotificationUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class NotificationService:FirebaseMessagingService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Main + serviceJob)

    @Inject lateinit var displayNotificationUseCase: DisplayNotificationUseCase

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(remoteMessage : RemoteMessage):Unit = with(remoteMessage){
        notification?.display(data)
    }

    private fun RemoteMessage.Notification.display(data: Map<String, String>) = serviceScope.launch(Main) {
        val notification = NotificationEntity(title, body, imageUrl?.toString())
        val params = DisplayNotificationUseCase.Params(notification, data)

        displayNotificationUseCase(params).fold({
            Timber.i("Notification displayed")
        }, { error ->
            Timber.e(error)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}