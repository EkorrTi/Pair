package com.example.pair.domain.usecase

abstract class BaseUseCase<Type, in Params> {
    abstract suspend fun invoke(params: Params): Type
}

object EmptyRequest