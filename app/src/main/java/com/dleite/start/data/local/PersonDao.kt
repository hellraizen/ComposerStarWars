package com.dleite.start.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonListings(
        personListingEntity: List<PersonListingEntity>
    )

    @Query("DELETE FROM personListingEntity")
    suspend fun clearPersonListings()

    @Query(
        """
            SELECT *
            FROM personListingEntity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' 
        """
    )
    suspend fun searchPersonListing(query: String): List<PersonListingEntity>
}