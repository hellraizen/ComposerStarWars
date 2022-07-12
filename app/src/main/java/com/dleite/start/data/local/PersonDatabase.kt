package com.dleite.start.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PersonListingEntity::class],
    version = 1
)
abstract class PersonDatabase : RoomDatabase() {
    abstract val dao: PersonDao
}