package com.hochupa.mycloset.utils

import android.content.Context
import android.content.SharedPreferences

class ProfileSharedPreferences(context: Context) {
    private val prefsFilename = "UserProfile"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var userEmail: String?
        get() = prefs.getString("userEmail", "")
        set(value) = prefs.edit().putString("userEmail", value).apply()

}