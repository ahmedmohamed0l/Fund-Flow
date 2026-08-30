package com.axoncodelabs.fundflow.util.language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun localizedContext(
        context: Context,
        language: Language
    ): Context {
        val locale = Locale.forLanguageTag(language.languageTag)

        val configuration = Configuration(
            context.resources.configuration
        ).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }

        return context.createConfigurationContext(configuration)
    }
}