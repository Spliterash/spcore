package ru.spliterash.spcore.domain.usecase

interface NonBlockingUseCase<I, O> {
    suspend fun execute(input: I): O
}