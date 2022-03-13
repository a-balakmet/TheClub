package com.the.club.common.sharedPreferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.the.club.common.sharedPreferences.PreferenceHelper.set
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = PreferenceHelper.customPrefs(context = context, name = "club_prefs")

    fun getStringValue(tag: String) = prefs.getString(tag, "") ?: ""

    fun getBooleanValue(tag: String) = prefs.getBoolean(tag, true)

    fun isValueExists(tag: String) = prefs.contains(tag)

    fun setValue (tag: String, value: Any) {
        prefs[tag] = value
    }
}