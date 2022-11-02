package top.heue.utils.base.util.file

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(
    context: Context,
    name: String,
    private var mode: Int = Context.MODE_PRIVATE
) {

    private var sp: SharedPreferences = context.getSharedPreferences(name, mode)

    fun <V> put(key: String, value: V) {
        val editor = sp.edit()
        when (value) {
            is String -> {
                editor.putString(key, value)
            }
            is Int -> {
                editor.putInt(key, value)
            }
            is Long -> {
                editor.putLong(key, value)
            }
            is Float -> {
                editor.putFloat(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
        }
        editor.apply()
    }


    @Suppress("UNCHECKED_CAST")
    fun <V> get(key: String, defValue: V): V? {
        when (defValue) {
            is String -> {
                return sp.getString(key, defValue)!! as V
            }
            is Int -> {
                return sp.getInt(key, defValue) as V
            }
            is Long -> {
                return sp.getLong(key, defValue) as V
            }
            is Float -> {
                return sp.getFloat(key, defValue) as V
            }
            is Boolean -> {
                return sp.getBoolean(key, defValue) as V
            }
        }
        return null
    }
}