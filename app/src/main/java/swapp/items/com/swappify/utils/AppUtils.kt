package swapp.items.com.swappify.utils

import android.content.Context
import android.os.Build
import java.io.IOException
import java.util.*




/**
 * Created by ravi on 05/10/17.
 */
class AppUtils {

    companion object {

        fun loadJSONFromAsset(context: Context, assetPath: String): String {

            try {
                val asset = context.assets.open(assetPath)
                val size = asset.available()

                val buffer = ByteArray(size)
                asset.read(buffer)
                asset.close()

                return String(buffer)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ""
        }

        fun getLocale(context: Context): Locale {
            val locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.resources.configuration.locales.get(0)
            } else {
                locale = context.resources.configuration.locale
            }
            return locale
        }

    }
}