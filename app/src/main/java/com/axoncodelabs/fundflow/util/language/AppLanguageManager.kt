package com.axoncodelabs.fundflow.util.language

import androidx.compose.runtime.mutableStateOf
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLanguageManager @Inject constructor() {

    @Volatile
    var currentLanguage: Language = Language.English
        private set

    var isLanguageSwitching = mutableStateOf(false)
        private set

    fun initialize(language: Language) {
        currentLanguage = language
        appLocale = Locale.forLanguageTag(language.languageTag)
    }

    fun setLanguage(language: Language) {
        isLanguageSwitching.value = true
        currentLanguage = language
        appLocale = Locale.forLanguageTag(language.languageTag)
    }

    fun finishLanguageSwitch() {
        isLanguageSwitching.value = false
    }
}