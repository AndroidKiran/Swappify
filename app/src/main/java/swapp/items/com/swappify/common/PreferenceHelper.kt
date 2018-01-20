package swapp.items.com.swappify.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class PreferenceHelper @Inject constructor(val context: Context?) {

    private var sharedPreference: SharedPreferences? = null

    init {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun getBooleanPreference(key: String?) = getBooleanPreference(key!!, false)

    fun getBooleanPreference(key: String?, defaultValue: Boolean?) = sharedPreference?.getBoolean(key, defaultValue!!)

    fun getStringPreference(key: String?) = sharedPreference?.getString(key, "")

    fun getStringPreference(key: String?, defaultValue: String?)= sharedPreference?.getString(key, defaultValue)

    fun getIntegerPreference(key: String?) = sharedPreference?.getInt(key, 0)

    fun getIntegerPreference(key: String?, defaultValue: Int) = sharedPreference?.getInt(key, defaultValue)

    fun getLongPreference(key: String?) = sharedPreference?.getLong(key, 0L)

    fun getLongPreference(key: String?, defaultValue: Long) = sharedPreference?.getLong(key, defaultValue)

    fun getFloatPreference(key: String?) = sharedPreference?.getFloat(key, 0f)

    fun getFloatPreference(key: String?, defaultValue: Long) = sharedPreference?.getFloat(key, defaultValue.toFloat())

    fun set(key: String?, value: Any?) {
        val sharedPreferenceEditor = sharedPreference?.edit()
        when (value) {
            is String -> sharedPreferenceEditor?.putString(key, value)
            is Long -> sharedPreferenceEditor?.putLong(key, value)
            is Int -> sharedPreferenceEditor?.putInt(key, value)
            is Boolean -> sharedPreferenceEditor?.putBoolean(key, value)
            is Float -> sharedPreferenceEditor?.putFloat(key, value)
        }
        sharedPreferenceEditor?.apply()
    }

    fun remove(key: String?) {
        sharedPreference?.edit()?.remove(key)?.apply()
    }

    fun clear() {
        sharedPreference?.edit()?.clear()?.apply()
    }

    fun contains(key: String?) = sharedPreference?.contains(key)
}