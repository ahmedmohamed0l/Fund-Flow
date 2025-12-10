package com.axoncodelabs.cashbox.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "funds")
data class FundEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val balance: Double,
    val createdAt: Long = System.currentTimeMillis(),
)