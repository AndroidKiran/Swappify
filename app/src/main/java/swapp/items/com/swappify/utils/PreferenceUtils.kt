package swapp.items.com.swappify.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Inject

class PreferenceUtils @Inject constructor(val context: Context): PreferencesHelper {

    val mSharedPreference: SharedPreferences

    init {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun getBooleanPreference(key: String): Boolean = getBooleanPreference(key, false)

    override fun getBooleanPreference(key: String, defaultValue: Boolean): Boolean = mSharedPreference.getBoolean(key, defaultValue!!)

    override fun getStringPreference(key: String): String = mSharedPreference.getString(key, "")

    override fun getStringPreference(key: String, defaultValue: String): String = mSharedPreference.getString(key, defaultValue)

    override fun getIntegerPreference(key: String): Int = mSharedPreference.getInt(key, 0)

    override fun getIntegerPreference(key: String, defaultValue: Int): Int = mSharedPreference.getInt(key, defaultValue)

    override fun getLongPreference(key: String): Long = mSharedPreference.getLong(key, 0L)

    override fun getLongPreference(key: String, defaultValue: Long): Long = mSharedPreference.getLong(key, defaultValue)

    override fun getFloatPreference(key: String): Float = mSharedPreference.getFloat(key, 0f)

    override fun getFloatPreference(key: String, defaultValue: Long): Float = mSharedPreference.getFloat(key, defaultValue.toFloat())

    override operator fun set(key: String, value: Any) {
        val sharedPreferenceEditor = mSharedPreference.edit()
        if (value is String) {
            sharedPreferenceEditor.putString(key, value)
        } else if (value is Long) {
            sharedPreferenceEditor.putLong(key, value)
        } else if (value is Int) {
            sharedPreferenceEditor.putInt(key, value)
        } else if (value is Boolean) {
            sharedPreferenceEditor.putBoolean(key, value)
        } else if (value is Float) {
            sharedPreferenceEditor.putFloat(key, value)
        }
        sharedPreferenceEditor.apply()
    }

    override fun remove(key: String) {
        mSharedPreference.edit().remove(key).apply()
    }

    override fun clear() {
        mSharedPreference.edit().clear().apply()
    }

    override operator fun contains(key: String): Boolean = mSharedPreference.contains(key)
}