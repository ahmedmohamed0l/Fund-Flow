package com.axoncodelabs.fundflow.data.util.backup

import com.axoncodelabs.fundflow.data.local.entity.FundEntity
import com.axoncodelabs.fundflow.data.local.entity.TransactionEntity
import kotlinx.serialization.Serializable

data class BackupInfo(
    val folderName: String,
    val dateTimeMillis: Long,
    val isAuto: Boolean? = null
)

@Serializable
data class BackupData(
    val version: Int = 1,
    val funds: List<FundEntity>,
    val transactions: List<TransactionEntity>
)