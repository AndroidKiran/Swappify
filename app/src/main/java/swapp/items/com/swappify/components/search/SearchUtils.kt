package swapp.items.com.swappify.components.search

import android.content.Context
import android.os.Build
import android.support.v4.view.ViewCompat
import java.util.*

internal object SearchUtils {

    val isRTL: Boolean
        get() = isRTL(Locale.getDefault())


    private fun isRTL(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.displayName[0]).toInt()
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt() || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
    }

    fun isRtlLayout(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.resources.configuration
                .layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL
    }

}
