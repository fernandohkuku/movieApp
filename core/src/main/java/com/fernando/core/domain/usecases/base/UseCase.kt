package com.fernando.core.domain.usecases.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.fernando.core.domain.usecases.base.Response.Success
import com.fernando.core.domain.usecases.base.Response.Failure

abstract class UseCase<T, in Input>(private val background: CoroutineDispatcher) {
    protected abstract suspend fun run(input: Input? = null): T

    suspend operator fun invoke(input: Input?=null):Response<T> = withContext(background){
        try {
            Success(run(input))
        }catch (error:Exception){
            Failure(error)
        }
    }
}