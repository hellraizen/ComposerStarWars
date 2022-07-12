package com.dleite.start.util.paginator

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}