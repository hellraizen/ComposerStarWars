package com.dleite.start.di

import android.app.Application
import androidx.room.Room
import com.dleite.start.data.local.PersonDatabase
import com.dleite.start.data.remote.StarWarsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerStockApi(): StarWarsApi{
        return  Retrofit.Builder()
            .baseUrl(StarWarsApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providerStockDatabase(app: Application):PersonDatabase{
        return Room.databaseBuilder(
            app,
            PersonDatabase::class.java,
            "Stockdb.db"
        ).build()
    }
}