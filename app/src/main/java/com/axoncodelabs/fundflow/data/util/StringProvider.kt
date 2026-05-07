package com.axoncodelabs.fundflow.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface StringProvider {
    fun getString(resId: Int): String
}

class StringProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}