package com.dleite.start.data.repository

import com.dleite.start.data.local.PersonDatabase
import com.dleite.start.data.mapper.toListPerson
import com.dleite.start.data.mapper.toListPersonEntity
import com.dleite.start.data.remote.StarWarsApi
import com.dleite.start.domain.model.Person
import com.dleite.start.domain.repository.PersonRepository
import com.dleite.start.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepositoryImpl @Inject constructor(
    private val api: StarWarsApi,
    private val db: PersonDatabase,
):PersonRepository {

    private val dao = db.dao

    override suspend fun getPersonListings(
        fetchFromRemote: Boolean,
        page: Int,
        query: String
    ): Flow<Resource<List<Person>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchPersonListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toListPerson() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                 api.getListingsPerson(page).toListPersonEntity()
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Algo deu errado ao tentar carregar..."))
                null
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Algo deu errado ao tentar carregar..."))
                null
            }

            remoteListings?.let { listings ->

                dao.clearPersonListings()
                dao.insertPersonListings(listings)
                emit(Resource.Success(
                    data = dao
                        .searchPersonListing("")
                        .map { it.toListPerson() }
                ))
                emit(Resource.Loading(false))

            }
        }
    }


}