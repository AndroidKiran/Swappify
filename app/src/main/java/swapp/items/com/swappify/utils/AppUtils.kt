package swapp.items.com.swappify.utils

import android.content.Context
import java.io.IOException

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
    }
}