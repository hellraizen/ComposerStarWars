package com.dleite.stockmarketapp.di

import android.app.Application
import androidx.room.Room
import com.dleite.stockmarketapp.data.local.StockDatabase
import com.dleite.stockmarketapp.data.remote.StockApi
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
    fun providerStockApi(): StockApi{
        return  Retrofit.Builder()
            .baseUrl(StockApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providerStockDatabase(app: Application):StockDatabase{
        return Room.databaseBuilder(
            app,
            StockDatabase::class.java,
            "Stockdb.db"
        ).build()
    }
}