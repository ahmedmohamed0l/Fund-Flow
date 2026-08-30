package com.axoncodelabs.fundflow.util.language

import java.util.Locale

internal var appLocale: Locale = Locale.US

enum class Language(val languageTag: String) {
    Arabic("ar"),
    English("en")
}