package ru.spliterash.spcore.domain.repo

interface SingletonRepository<T> {
    suspend fun get(): T?
    suspend fun set(t: T)
}