package com.dleite.stockmarketapp.data.repository

import com.dleite.stockmarketapp.data.csv.CSVParser
import com.dleite.stockmarketapp.data.local.StockDatabase
import com.dleite.stockmarketapp.data.mapper.toCompanyInfo
import com.dleite.stockmarketapp.data.mapper.toCompanyListing
import com.dleite.stockmarketapp.data.remote.StockApi
import com.dleite.stockmarketapp.domain.model.CompanyInfo
import com.dleite.stockmarketapp.domain.model.CompanyListing
import com.dleite.stockmarketapp.domain.model.IntradayInfo
import com.dleite.stockmarketapp.domain.repository.StockRepository
import com.dleite.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayParser: CSVParser<IntradayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Algo deu errado ao tentar carregar..."))
                null

            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Algo deu errado ao tentar carregar..."))
                null
            }

            remoteListings?.let { listings ->

                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListing() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))

            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val results = intradayParser.parse(response.byteStream())
            Resource.Success(results)

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Algo deu errado ao tentar carregar das infoday ...")

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Algo deu errado ao tentar carregar das infoday ...")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Algo deu errado ao tentar carregar das company info ...")

        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Algo deu errado ao tentar carregar das company info ...")
        }
    }

}