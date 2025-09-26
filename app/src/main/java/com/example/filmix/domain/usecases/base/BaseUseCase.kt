package com.example.filmix.domain.usecases.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseUseCase<in P, R> {

    operator fun invoke(parameters: P): Flow<Result<R>> = flow {
        emit(Result.Loading())
        try {
            val result = execute(parameters)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }

    protected abstract suspend fun execute(parameters: P): R
}

abstract class NoParamsUseCase<R> {

    operator fun invoke(): Flow<Result<R>> = flow {
        emit(Result.Loading())
        try {
            val result = execute()
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }

    protected abstract suspend fun execute(): R
}

sealed class Result<out T> {
    data class Loading<T>(val data: T? = null) : Result<T>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val message: String, val data: T? = null) : Result<T>()
}
