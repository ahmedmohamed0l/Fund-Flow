package com.axoncodelabs.cashbox.data.util.backup

import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.local.entity.TransactionEntity
import kotlinx.serialization.Serializable

data class BackupInfo(
    val folderName: String,
    val dateTimeMillis: Long
)

@Serializable
data class BackupData(
    val version: Int = 1,
    val funds: List<FundEntity>,
    val transactions: List<TransactionEntity>
)