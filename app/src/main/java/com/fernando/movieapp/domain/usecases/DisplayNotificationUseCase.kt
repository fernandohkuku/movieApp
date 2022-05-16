package com.fernando.movieapp.domain.usecases

import com.fernando.core.domain.entities.NotificationEntity
import com.fernando.core.domain.managers.NotificationsManager
import com.fernando.core.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class DisplayNotificationUseCase @Inject constructor(
    private val handler: NotificationsManager,
    background: CoroutineDispatcher
) : UseCase<Unit, DisplayNotificationUseCase.Params>(background) {
    override suspend fun run(input: Params?) {
        requireNotNull(input) { "Params can't be null" }

        val data = input.data
        val notification = input.notification

        if (data.isNotEmpty()) {
            Timber.d("Message data payload: %s", data)
            notification.setData(data)
        }

        handler.display(notification)
    }

    class Params(
        val notification: NotificationEntity,
        val data: Map<String, String>
    )
}
