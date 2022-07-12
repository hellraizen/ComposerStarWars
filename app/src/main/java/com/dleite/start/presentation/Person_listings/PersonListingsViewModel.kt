package com.dleite.start.presentation.Person_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dleite.start.domain.model.Person
import com.dleite.start.domain.repository.PersonRepository
import com.dleite.start.util.Resource
import com.dleite.start.util.paginator.PersonSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 10

@HiltViewModel
class PersonListingsViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    var state by mutableStateOf(PersonListingsState())

    private var searchJob: Job? = null

    init {
        getPersonList()
    }

    private fun getPersonList() {
        state = state.copy(personList = Pager(PagingConfig(pageSize = NETWORK_PAGE_SIZE)) {
            PersonSource(repository)
        }.flow.cachedIn(viewModelScope))
    }

    fun onEvent(event: PersonListingsEvent) {
        when (event) {
            is PersonListingsEvent.Refresh -> {
                getPersonListings(fetchFromRemote = true)
            }
            is PersonListingsEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getPersonListings()
                }
            }
        }
    }


    private fun getPersonListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = true,
    ) {
        viewModelScope.launch {

            repository
                .getPersonListings(fetchFromRemote, 1, query)
                .collect() { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let {
                            }
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                error = result.message,
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }

}