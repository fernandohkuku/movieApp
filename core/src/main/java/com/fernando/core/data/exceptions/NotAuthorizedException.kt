package com.fernando.core.data.exceptions

class NotAuthorizedException(override val message: String = "Not authorized") : ApiException(message)
