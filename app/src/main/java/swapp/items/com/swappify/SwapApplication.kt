package swapp.items.com.swappify

import android.support.multidex.MultiDexApplication
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by ravi on 20/08/17.
 */

class SwapApplication : MultiDexApplication() {

    private val FONT_PATH : String = "fonts/roboto_condensed_regular.ttf"

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(FONT_PATH)
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    companion object {
        lateinit var mInstance: SwapApplication
    }
}