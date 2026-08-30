package com.axoncodelabs.fundflow.util.language

import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLanguageManager @Inject constructor() {

    @Volatile
    var currentLanguage: Language = Language.English
        private set

    fun initialize(language: Language) {
        currentLanguage = language

        appLocale = Locale.forLanguageTag(language.languageTag)
    }

    fun setLanguage(language: Language) {
        currentLanguage = language

        appLocale = Locale.forLanguageTag(language.languageTag)
    }
}