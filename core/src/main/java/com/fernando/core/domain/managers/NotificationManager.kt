package com.fernando.core.domain.managers

import com.fernando.core.domain.entities.NotificationEntity

interface NotificationsManager {
    suspend fun display(notification: NotificationEntity)
}