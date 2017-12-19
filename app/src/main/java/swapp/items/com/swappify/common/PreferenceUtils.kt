package swapp.items.com.swappify.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

@PerActivity
class PreferenceUtils @Inject constructor(val context: Context?) {

    private val mSharedPreference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getBooleanPreference(key: String?): Boolean? = getBooleanPreference(key!!, false)

    fun getBooleanPreference(key: String?, defaultValue: Boolean?): Boolean? = mSharedPreference.getBoolean(key, defaultValue!!)

    fun getStringPreference(key: String?): String? = mSharedPreference.getString(key, "")

    fun getStringPreference(key: String?, defaultValue: String?): String? = mSharedPreference.getString(key, defaultValue)

    fun getIntegerPreference(key: String?): Int = mSharedPreference.getInt(key, 0)

    fun getIntegerPreference(key: String?, defaultValue: Int): Int = mSharedPreference.getInt(key, defaultValue)

    fun getLongPreference(key: String?): Long = mSharedPreference.getLong(key, 0L)

    fun getLongPreference(key: String?, defaultValue: Long): Long = mSharedPreference.getLong(key, defaultValue)

    fun getFloatPreference(key: String?): Float = mSharedPreference.getFloat(key, 0f)

    fun getFloatPreference(key: String?, defaultValue: Long): Float = mSharedPreference.getFloat(key, defaultValue.toFloat())

    operator fun set(key: String?, value: Any?) {
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

    fun remove(key: String?) {
        mSharedPreference.edit().remove(key).apply()
    }

    fun clear() {
        mSharedPreference.edit().clear().apply()
    }

    operator fun contains(key: String?): Boolean = mSharedPreference.contains(key)
}