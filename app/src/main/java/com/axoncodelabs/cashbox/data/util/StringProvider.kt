package com.axoncodelabs.cashbox.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

interface StringProvider {
    fun getString(resId: Int): String
}

class StringProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}
