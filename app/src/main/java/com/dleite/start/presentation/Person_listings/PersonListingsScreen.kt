package com.dleite.start.presentation.Person_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.dleite.start.domain.model.Person
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun PersonListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: PersonListingsViewModel = hiltViewModel(),
) {

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val state = viewModel.state
    val personListItems: LazyPagingItems<Person> = state.personList.collectAsLazyPagingItems()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery, onValueChange = {
                viewModel.onEvent(
                    PersonListingsEvent.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Procurar por nome ...")
            },
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(personListItems) { item ->
                    item?.let {
                        PersonItem(
                            person = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /*TODO*/ }
                                .padding(16.dp)
                        )
                    }
                }
//                itens { i ->
//                    val person = state.persons[i]
//                    if (i >= state.persons.size - 1 && !state.endReached && !state.isLoading) {
//                        viewModel.getPersons()
//                    }
//                    PersonItem(
//                        person = person,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable { /*TODO*/ }
//                            .padding(16.dp)
//                    )
//                    if (i < state.persons.size) {
//                        Divider(
//                            modifier = Modifier.padding(
//                                horizontal = 16.dp
//                            )
//                        )
//                    }
//                }
                item {
                    if (state.isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }
    }


}