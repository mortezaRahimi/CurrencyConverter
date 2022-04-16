package com.mortex.converter.local

import android.content.SharedPreferences
import com.mortex.converter.local.Constants.PREF_BALANCE
import javax.inject.Inject

class SessionManager @Inject constructor(private val preferences: SharedPreferences) {

    fun getBalance() = preferences.getString(PREF_BALANCE, null)

    fun setBalance(value: String) {
        val editor = preferences.edit()
        editor.putString(PREF_BALANCE, value)
        editor.apply()
    }


}