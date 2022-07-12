package com.dleite.start.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonListingEntity(
    val gender: String,
    val name: String,
    val url: String,
    @PrimaryKey val id: Int? = null
)