package com.dleite.stockmarketapp.di

import com.dleite.stockmarketapp.data.csv.CSVParser
import com.dleite.stockmarketapp.data.csv.CompanyListingsParser
import com.dleite.stockmarketapp.data.csv.IntradayParser
import com.dleite.stockmarketapp.data.repository.StockRepositoryImpl
import com.dleite.stockmarketapp.domain.model.CompanyListing
import com.dleite.stockmarketapp.domain.model.IntradayInfo
import com.dleite.stockmarketapp.domain.repository.StockRepository
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
    abstract fun bindCompanyListingsParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayParser(
        intradayParser: IntradayParser
    ): CSVParser<IntradayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ):StockRepository



}