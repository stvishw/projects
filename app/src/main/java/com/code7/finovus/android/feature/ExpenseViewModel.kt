package com.code7.finovus.android.feature.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.code7.finovus.android.data.dao.ExpenseDao
import com.code7.finovus.android.data.model.ExpenseEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseViewModel @Inject constructor(private val dao: ExpenseDao) : ViewModel() {

    // State to hold the list of expenses
    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> get() = _expenses

    // State to hold the total balance
    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> get() = _totalBalance

    // State for notification messages
    private val _notificationMessage = MutableStateFlow("")
    val notificationMessage: StateFlow<String> get() = _notificationMessage

    init {
        // Fetch expenses from the database
        fetchExpenses()
    }

    // Function to fetch expenses from the database
    private fun fetchExpenses() {
        viewModelScope.launch {
            dao.getAllExpense().collect { expenseList ->
                _expenses.value = expenseList
                calculateTotalBalance(expenseList)
            }
        }
    }

    // Calculate the total balance from the expense list
    private fun calculateTotalBalance(expenseList: List<ExpenseEntity>) {
        val newTotal = expenseList.sumOf { it.amount }

        // If the balance has changed, notify and update the state
        if (newTotal != _totalBalance.value) {
            _totalBalance.value = newTotal
            sendBalanceNotification(newTotal)
        }
    }

    // Function to send a notification when the total balance changes
    private fun sendBalanceNotification(newBalance: Double) {
        _notificationMessage.value = "Your total balance has been updated to $newBalance."
        // Additional notification logic can be implemented here if required
    }

    // Add or update an expense in the database
    fun addOrUpdateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.insertExpense(expense)
        }
    }

    // Delete an expense from the database
    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
        }
    }
}
