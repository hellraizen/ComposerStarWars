package com.dleite.start.presentation.Person_listings

import androidx.paging.PagingData
import com.dleite.start.domain.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PersonListingsState(
    val personList: Flow<PagingData<Person>> = emptyFlow(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
)