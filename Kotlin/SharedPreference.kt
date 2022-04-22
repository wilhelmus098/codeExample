package io.github.wilhelmus098.client_app

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreference {
    internal val USER_NAME = "user"

    internal fun getSharedPreference(ctx:Context):SharedPreferences
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setUser(ctx: Context, username:String)
    {
        val editor = getSharedPreference(ctx).edit()
        editor.putString(USER_NAME, username)
        editor.commit()
    }

    fun getUser(ctx: Context):String
    {
        return getSharedPreference(ctx).getString(USER_NAME, "")
    }

    fun removeUser(ctx: Context)
    {
        val editor = getSharedPreference(ctx).edit()
        editor.clear()
        editor.commit()
    }
}