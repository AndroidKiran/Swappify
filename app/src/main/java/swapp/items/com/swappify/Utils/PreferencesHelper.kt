package swapp.items.com.swappify.Utils

/**
 * Created by ravi on 02/10/17.
 */
interface PreferencesHelper {

    fun getBooleanPreference(key: String): Boolean

    fun getBooleanPreference(key: String, defaultValue: Boolean): Boolean

    fun getStringPreference(key: String): String

    fun getStringPreference(key: String, defaultValue: String): String

    fun getIntegerPreference(key: String): Int

    fun getIntegerPreference(key: String, defaultValue: Int): Int

    fun getLongPreference(key: String): Long

    fun getLongPreference(key: String, defaultValue: Long): Long

    fun getFloatPreference(key: String): Float

    fun getFloatPreference(key: String, defaultValue: Long): Float

    operator fun set(key: String, value: Any)

    fun remove(key: String)

    fun clear()

    operator fun contains(key: String): Boolean

}