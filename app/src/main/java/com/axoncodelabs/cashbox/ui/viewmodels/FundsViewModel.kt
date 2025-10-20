package com.axoncodelabs.cashbox.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axoncodelabs.cashbox.data.local.entity.FundEntity
import com.axoncodelabs.cashbox.data.repository.CashBoxRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FundsViewModel @Inject constructor(
    private val repository: CashBoxRepository
) : ViewModel() {
    val funds = repository.getAllFunds().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Dialogs States
    private val _showAddFundDialog = mutableStateOf(false)
    val showAddFundDialog: State<Boolean> = _showAddFundDialog

    private val _showTransferDialog = mutableStateOf(false)
    val showTransferFundDialog: State<Boolean> = _showTransferDialog

    private val _selectedFund = mutableStateOf<FundEntity?>(null)
    val selectedFund: State<FundEntity?> = _selectedFund

    // Actions
    fun showAddFundDialog() {
        _showAddFundDialog.value = true
    }

    fun hideAddFundDialog() {
        _showAddFundDialog.value = false
    }

    fun showTransferDialog() {
        _showTransferDialog.value = true
    }

    fun hideTransferDialog() {
        _showTransferDialog.value = false
    }

    fun selectFund(fund: FundEntity?) {
        _selectedFund.value = fund
    }

    suspend fun addFund(name: String, balance: Double): Result<Boolean> {
        return try {
            repository.insertFund(FundEntity(name = name, balance = balance))
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFund(fund: FundEntity): Result<Boolean> {
        return try {
            repository.updateFund(fund)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteFund(fund: FundEntity): Result<Boolean> {
        return try {
            repository.deleteFund(fund)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun transferFunds(
        fromFundId: Int,
        toFundId: Int,
        amount: Double,
        description: String,
    ): Result<Boolean> {
        return try {
            repository.transferBetweenFunds(fromFundId, toFundId, amount, description)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}