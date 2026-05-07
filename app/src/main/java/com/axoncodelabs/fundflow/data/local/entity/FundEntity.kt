package com.axoncodelabs.fundflow.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "funds")
data class FundEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val balance: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val isExcepted: Boolean = false,
)