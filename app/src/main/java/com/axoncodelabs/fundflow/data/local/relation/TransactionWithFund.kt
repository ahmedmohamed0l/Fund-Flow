package com.axoncodelabs.fundflow.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity

data class TransactionWithFund(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "fundId",
        entityColumn = "id"
    )
    val fund: FundEntity
)