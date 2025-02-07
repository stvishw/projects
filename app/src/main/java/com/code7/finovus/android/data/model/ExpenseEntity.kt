package com.code7.finovus.android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val type: String,
    val synced: Boolean = false // Default to false if not provided
)

//@Entity(tableName = "expense_table")
//data class ExpenseEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int? = null,
//    val title: String,
//    val amount: Double,
//    val type: String,
//    val date: String,
//    val synced: Boolean = false // Add this field
//)
