package com.fernando.core.domain.entities

data class NotificationEntity(
    val title: String?,
    val body: String?,
    val imageUrl: String?
) {
    companion object {
        private const val KEY_LINK = "link"
    }

    var link: String? = null

    fun setData(data: Map<String, String>) {
        link = data[KEY_LINK]
    }
}