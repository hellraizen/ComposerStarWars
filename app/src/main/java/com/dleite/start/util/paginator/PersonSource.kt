package com.dleite.start.util.paginator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dleite.start.domain.model.Person
import com.dleite.start.domain.repository.PersonRepository
import com.dleite.start.util.Resource
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import java.io.IOException

class PersonSource(
    private val repository: PersonRepository
) : PagingSource<Int, Person>() {

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val nextPage: Int = params.key ?: FIRST_PAGE_INDEX

        return try {
            val response = repository.getPersonListings(true,nextPage,"")
            var error = false
            var dataResponse:List<Person> = emptyList()
            response.collect(){ result ->
                when(result){
                    is Resource.Success-> result.data?.let { dataResponse = it }
                    is Resource.Error-> error= true
                }
            }

            LoadResult.Page(
                data = dataResponse,
                prevKey = if (nextPage == FIRST_PAGE_INDEX) null else nextPage,
                nextKey = if (dataResponse.isEmpty() || error) null else nextPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}