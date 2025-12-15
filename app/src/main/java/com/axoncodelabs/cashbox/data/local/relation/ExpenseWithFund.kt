package com.axoncodelabs.cashbox.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity

data class ExpenseWithFund(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "fundId",
        entityColumn = "id"
    )
    val fund: FundEntity
)