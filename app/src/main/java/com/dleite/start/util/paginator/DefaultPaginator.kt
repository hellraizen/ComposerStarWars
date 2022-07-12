package com.dleite.start.util.paginator

import com.dleite.start.util.Resource
import kotlinx.coroutines.flow.Flow

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Flow<Resource<List<Item>>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (String?) -> Unit,
    private inline val onSuccess: suspend (item: List<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        result.collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        currentKey = getNextKey(it)
                        onSuccess(it, currentKey)
                        onLoadUpdated(false)
                    }
                }
                is Resource.Error -> {
                    onError(result.message)
                }
                is Resource.Loading -> {
                    onLoadUpdated(result.isLoading)
                }
            }
        }
    }

    override fun reset() {
        currentKey = initialKey
    }
}