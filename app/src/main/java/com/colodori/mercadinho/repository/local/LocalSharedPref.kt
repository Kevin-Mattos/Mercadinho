package com.colodori.mercadinho.repository.local

import android.content.SharedPreferences


const val USER_NAME = "SHARED_PREF_USER_NAME"
const val USER_PREF = "USER_PREF"
object LocalSharedPref {

    lateinit var sharedPreferences: SharedPreferences

    var userName: String? = null
    get() = sharedPreferences.getString(field, null)
    set(value) = sharedPreferences.edit().putString(USER_NAME, value).apply()

}