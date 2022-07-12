package com.dleite.start.domain.repository

import com.dleite.start.domain.model.Person
import com.dleite.start.util.Resource
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

    suspend fun getPersonListings(
        fetchFromRemote: Boolean,
        page: Int,
        query: String
    ): Flow<Resource<List<Person>>>
}