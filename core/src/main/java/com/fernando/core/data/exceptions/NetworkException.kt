package com.fernando.core.data.exceptions

class NetworkException(
    override val message: String = "something is went wrong"
) : ApiException(message)