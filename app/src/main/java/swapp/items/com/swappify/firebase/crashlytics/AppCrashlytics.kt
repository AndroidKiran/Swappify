package swapp.items.com.swappify.firebase.crashlytics

import com.google.firebase.crash.FirebaseCrash
import swapp.items.com.swappify.injection.scopes.PerActivity
import javax.inject.Inject

/**
 * Created by ravi on 03/10/17.
 */
@PerActivity
class AppCrashlytics @Inject constructor() {

    fun reportError(throwable: Throwable, vararg args: Any) {
        if (args.isNotEmpty()) {
            FirebaseCrash.log(argumentsAsString(*args))
        }
        FirebaseCrash.report(throwable)
    }

    fun argumentsAsString(vararg args: Any): String {
        val builder = StringBuilder()
        val length = args.size
        for (i in 0 until length) {
            val arg = args[i]
            builder.append(arg)
            if (i < length - 1) {
                builder.append(" : ")
            }

        }
        return builder.toString()
    }
}