package jp.sabiz.android.soundswitch

import android.content.Context
import android.content.SharedPreferences

class DataStorage internal constructor(context: Context,storageName: String){

    private val preference: SharedPreferences

    init {
        preference = context.getSharedPreferences(storageName,Context.MODE_PRIVATE)
    }

    fun store(key: Int, value: Int) {
        val edit = preference.edit()
        edit.putInt(key.toString(16),value)
        edit.apply()
    }

    fun restore(key: Int, def: Int):Int = preference.getInt(key.toString(16),def)
}
