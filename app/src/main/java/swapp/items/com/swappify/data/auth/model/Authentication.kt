package swapp.items.com.swappify.data.auth.model

import swapp.items.com.swappify.data.user.model.User

data class Authentication constructor(private val user: User?, private val failure: Throwable?) {

    val isSuccess: Boolean
        get() = user != null

    fun getUser(): User {
        if (user == null) {
            throw IllegalStateException("Authentication is failed please check with isSuccess first")
        }
        return user
    }

    fun getFailure(): Throwable {
        if (failure == null) {
            throw IllegalStateException("Authentication is successful please check with isSuccess first")
        }
        return failure
    }

}
