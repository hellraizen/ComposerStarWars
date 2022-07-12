package com.dleite.start.presentation.Person_listings

sealed class PersonListingsEvent {
    object Refresh: PersonListingsEvent()
    data class  OnSearchQueryChange(val query: String) : PersonListingsEvent()
}