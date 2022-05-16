package com.fernando.core.data.exceptions

class BadRequestException(
    val type: String? = null,
    override val message: String
) : ApiException(message)

