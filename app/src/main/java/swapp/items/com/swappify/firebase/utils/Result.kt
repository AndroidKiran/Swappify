package swapp.items.com.swappify.firebase.utils

data class Result<out T>(private val data: T?, private var throwable: Throwable?) {

    fun isSuccess(): Boolean = data != null

    val error: Throwable?
        get() {
            if (throwable == null) {
                throw IllegalStateException("Database operation is successful please check with isSuccess first")
            }
            return throwable
        }

    val value: T
        get() {
            if (data == null) {
                throw IllegalStateException("Database operation is not successful please check with isSuccess first")
            }
            return data
        }
}
