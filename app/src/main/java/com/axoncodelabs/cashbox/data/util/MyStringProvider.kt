package com.axoncodelabs.cashbox.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.DecimalFormat
import javax.inject.Inject

interface MyStringProvider {
    fun getString(resId: Int): String
}

class MyStringProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MyStringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}

fun myDoubleFormat(double: Double): String {
    val formatter = DecimalFormat("#,##0.##")
    return formatter.format(double)
}