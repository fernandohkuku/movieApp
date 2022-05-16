package com.fernando.core.infrastructure.error

import com.fernando.core.data.exceptions.BadRequestException
import com.fernando.core.data.exceptions.NotAuthorizedException
import com.fernando.core.data.exceptions.NotFoundException
import com.fernando.core.data.exceptions.ServerException
import com.fernando.core.data.models.ApiError
import com.fernando.ui_ktx.exception.ignoreRuntimeExceptions
import com.google.gson.Gson
import java.io.Reader
import java.net.HttpURLConnection
import javax.inject.Inject

class ApiErrorHandler @Inject constructor(private val gson: Gson) : ErrorHandler {
    companion object {
        private const val HTTP_SYSTEM_CONSTRAINTS = 422
    }

    override fun throwFromCode(errorCode: Int, reader: Reader?) {
        when (errorCode) {
            HttpURLConnection.HTTP_FORBIDDEN -> {
                throw NotAuthorizedException("Not authorized to realize this action.")
            }
            HttpURLConnection.HTTP_BAD_REQUEST -> reader.parseToApiError()?.let {
                throw BadRequestException(type = it.message, message = it.message.toString())
            }

            HttpURLConnection.HTTP_NOT_FOUND -> reader.parseToApiError()?.let {
                throw NotFoundException(message = it.message.toString())
            }

            HttpURLConnection.HTTP_INTERNAL_ERROR, HttpURLConnection.HTTP_UNAVAILABLE -> {
                throw ServerException("Sorry, there was a problem in the server. Try again later.")
            }
        }
    }

    private fun Reader?.parseToApiError() = ignoreRuntimeExceptions<ApiError> {
        gson.fromJson(this, ApiError::class.java)
    }

}