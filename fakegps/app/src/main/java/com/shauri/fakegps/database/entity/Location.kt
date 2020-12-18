package com.shauri.fakegps.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "locations")
data class Location(@PrimaryKey(autoGenerate = true)
                    val id: Int? = null,
                    val lat: Double,
                    val lon :Double,
                    val name: String) {
}