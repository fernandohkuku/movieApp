package com.fernando.core.infrastructure.error

import java.io.Reader

interface ErrorHandler {
    fun throwFromCode(errorCode: Int, reader: Reader?)
}
