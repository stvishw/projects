package com.code7.finovus.android.utils

import com.code7.finovus.android.R
import com.code7.finovus.android.data.model.ExpenseEntity
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Utils {

    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDateForChart(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    //    fun formatCurrency(amount: Double, locale: Locale = Locale.US): String {
//        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
//        return currencyFormatter.format(amount)
//    }
    fun formatCurrency(amount: Double, locale: Locale = Locale("en", "IN")): String {
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(amount)
    }

    fun formatDayMonthYear(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDayMonth(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatToDecimalValue(d: Double): String {
        return String.format("%.2f", d)
    }

    fun formatStringDateToMonthDayYear(date: String): String {
        val millis = getMillisFromDate(date)
        return formatDayMonthYear(millis)
    }

    fun getMillisFromDate(date: String): Long {
        return getMilliFromDate(date)
    }

    fun getMilliFromDate(dateFormat: String?): Long {
        var date: Date? = null
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
            // Handle the error, maybe return a default value
            return 0L // or any other default value indicating failure
        }
        println("Today is $date")
        return date.time
    }


    fun getItemIcon(item: ExpenseEntity): Int {
        return if (item.title == "Paypal") {
            R.drawable.ic_paypal
        } else if (item.title == "Subscriptions") {
            R.drawable.ic_netflix
        } else if (item.title == "Starbucks") {
            R.drawable.ic_starbucks
        }
        else if (item.title == "Salary") {
            R.drawable.salary
        }
        else if (item.title == "Freelance") {
            R.drawable.ic_starbucks
        }
        else if (item.title == "Investments") {
            R.drawable.investment
        }
        else if (item.title == "Bonus") {
            R.drawable.bonus
        }
        else if (item.title == "Rental Income") {
            R.drawable.rental
        }
        else if (item.title == "Other Income") {
            R.drawable.other_income
        }
        else if (item.title == "Bank Account Transfer") {
            R.drawable.bank_transfer
        }
        else if (item.title == "Grocery") {
            R.drawable.grocery
        }
        else if (item.title == "Rent") {
            R.drawable.rent
        }

        else if (item.title == "Shopping") {
            R.drawable.shopping
        }
        else if (item.title == "Transport") {
            R.drawable.transport
        }
        else if (item.title == "Utilities") {
            R.drawable.utilities
        }
        else if (item.title == "Food") {
            R.drawable.food
        }
        else if (item.title == "Entertainment") {
            R.drawable.entertainment
        }
        else if (item.title == "Education") {
            R.drawable.education
        }
        else if (item.title == "Healthcare") {
            R.drawable.healthcare
        }
        else if (item.title == "Insurance") {
            R.drawable.insurance
        }
        else if (item.title == "Debt Payments") {
            R.drawable.payments
        }
        else if (item.title == "Gifts & Donations") {
            R.drawable.gifts
        }
        else if (item.title == "Travel") {
            R.drawable.travel
        }

        else if (item.title == "Paytm") {
            R.drawable.travel
        }

        else  {
            R.drawable.payt3
        }
    }

}