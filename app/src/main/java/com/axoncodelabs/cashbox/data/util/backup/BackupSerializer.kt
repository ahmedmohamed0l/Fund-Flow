package com.axoncodelabs.cashbox.data.util.backup

import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackupSerializer @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun serialize(data: BackupData): String? {
        return try {
            json.encodeToString(data)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deserialize(jsonString: String): BackupData? {
        return try {
            val rawData: BackupData = json.decodeFromString(jsonString)

            val cleanFunds = rawData.funds.filter { it.id != -1 }
            val cleanTransactions = rawData.transactions.filter { it.id != -1 }

            rawData.copy(
                funds = cleanFunds,
                transactions = cleanTransactions
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}