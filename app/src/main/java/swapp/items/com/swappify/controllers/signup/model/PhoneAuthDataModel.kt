package swapp.items.com.swappify.controllers.signup.model

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import swapp.items.com.swappify.data.user.model.User

class PhoneAuthDataModel private constructor(var state: Int,
                                             val firebaseException: FirebaseException?,
                                             val phoneAuthCredential: PhoneAuthCredential?,
                                             val verificationId: String?,
                                             val token: PhoneAuthProvider.ForceResendingToken?,
                                             val user: User?) {

    private constructor(builder: Builder) : this(builder.state, builder.firebaseException,
            builder.phoneAuthCredential, builder.verificationId, builder.token, builder.user)

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder private constructor(var state: Int) {

        var firebaseException: FirebaseException? = null
        var phoneAuthCredential: PhoneAuthCredential? = null
        var verificationId: String? = null
        var token: PhoneAuthProvider.ForceResendingToken? = null
        var user: User? = null

        constructor(init: Builder.() -> Unit) : this(0) {
            init()
        }

        fun state(init: Builder.() -> Int) = apply { state = init() }

        fun firebaseException(init: Builder.() -> FirebaseException) = apply { firebaseException = init() }

        fun phoneAuthCredential(init: Builder.() -> PhoneAuthCredential) = apply { phoneAuthCredential = init() }

        fun verificationId(init: Builder.() -> String) = apply { verificationId = init() }

        fun token(init: Builder.() -> PhoneAuthProvider.ForceResendingToken) = apply { token = init() }

        fun currentUser(init: Builder.() -> User) = apply { user = init() }

        fun build() = PhoneAuthDataModel(this@Builder)

    }
}

