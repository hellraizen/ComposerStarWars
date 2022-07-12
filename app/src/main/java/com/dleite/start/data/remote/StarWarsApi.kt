package com.dleite.start.data.remote

import com.dleite.start.data.remote.dto.ResultListApi
import retrofit2.http.GET
import retrofit2.http.Query

interface StarWarsApi {

    @GET("api/people")
    suspend fun getListingsPerson(
        @Query("page") page: Int
    ):ResultListApi


    companion object{
        const val BASE_URL = "https://swapi.dev"
    }
}