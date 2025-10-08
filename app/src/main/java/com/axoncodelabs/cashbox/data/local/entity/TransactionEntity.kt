package com.axoncodelabs.cashbox.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val description: String,
    val date: Long = System.currentTimeMillis(),
    val type: TransactionType,
    val fundId: Int,
    val isTransfer: Boolean = false,
)

enum class TransactionType {
    INCOME, EXPENSE
}