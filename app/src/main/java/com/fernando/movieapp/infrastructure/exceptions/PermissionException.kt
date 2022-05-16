package com.fernando.movieapp.infrastructure.exceptions

internal open class PermissionException(
    override val message: String = "You don't have enough permissions"
) : RuntimeException(message)
