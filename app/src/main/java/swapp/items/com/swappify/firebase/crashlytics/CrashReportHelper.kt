package swapp.items.com.swappify.firebase.crashlytics

interface CrashReportHelper {

    fun reportError(throwable: Throwable, vararg args: Any)

    fun argumentsAsString(vararg args: Any): String
}