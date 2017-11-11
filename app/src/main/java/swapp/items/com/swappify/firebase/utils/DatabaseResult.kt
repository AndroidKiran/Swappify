package swapp.items.com.swappify.firebase.utils

import io.reactivex.functions.Function

data class DatabaseResult<T>(private var failure: Throwable?) {

    companion object {
        fun <T> errorAsDatabaseResult(): Function<Throwable, DatabaseResult<T>> =
                Function<Throwable, DatabaseResult<T>> { throwable -> DatabaseResult<T>(
                        throwable ?: Throwable("Database error is missing")) }
    }

    private var data: T? = null

    constructor(data: T) : this(null) {
        this.data = data
    }

    fun isSuccess(): Boolean = data != null

    fun getFailure(): Throwable? {
        if (failure == null) {
            throw IllegalStateException("Database write is successful please check with isSuccess first")
        }
        return failure
    }

    fun getData(): T? {
        if (data == null) {
            throw IllegalStateException("Database write is not successful please check with isSuccess first")
        }
        return data
    }
}
