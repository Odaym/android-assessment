package com.saltserv.assessment

import android.content.Context
import android.content.SharedPreferences

interface PrefStore {
    fun setAuthToken(authToken: String)

    fun getAuthToken(): String

    fun removeAuthToken()

    fun isLoggedIn(): Boolean
}

class PrefStoreImpl constructor(context: Context) : PrefStore {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("shared preferences", 0)

    override fun setAuthToken(authToken: String) {
        sharedPreferences.edit()
            .putString("token", "Bearer $authToken")
            .apply()
    }

    override fun getAuthToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }

    override fun removeAuthToken() {
        sharedPreferences.edit()
            .remove("token")
            .apply()
    }

    override fun isLoggedIn(): Boolean {
        return sharedPreferences.getString("token", "") != ""
    }
}