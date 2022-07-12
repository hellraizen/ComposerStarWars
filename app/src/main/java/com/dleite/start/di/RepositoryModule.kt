package com.dleite.start.di

import com.dleite.start.data.repository.PersonRepositoryImpl
import com.dleite.start.domain.repository.PersonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: PersonRepositoryImpl
    ):PersonRepository
}