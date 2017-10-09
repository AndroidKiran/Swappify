package swapp.items.com.swappify.controllers.signup.model

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneAuthDataModel private constructor(state: Int,
                                             firebaseException: FirebaseException,
                                             phoneAuthCredential: PhoneAuthCredential,
                                             verificationId: String,
                                             token: PhoneAuthProvider.ForceResendingToken) {

    private constructor(builder: Builder) : this (builder.state, builder.firebaseException,
            builder.phoneAuthCredential, builder.verificationId, builder.token)

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }

    class Builder private constructor(var state: Int) {

        lateinit var firebaseException: FirebaseException
        lateinit var phoneAuthCredential: PhoneAuthCredential
        lateinit var verificationId: String
        lateinit var token: PhoneAuthProvider.ForceResendingToken

        constructor(init: Builder.() -> Unit): this(0) {
            init()
        }

        fun state(init: Builder.() -> Int) = apply { state = init() }

        fun firebaseException(init: Builder.() -> FirebaseException) = apply { firebaseException = init() }

        fun phoneAuthCredential(init: Builder.() -> PhoneAuthCredential) = apply { phoneAuthCredential = init() }

        fun verificationId(init: Builder.() -> String) = apply { verificationId = init() }

        fun token(init: Builder.() -> PhoneAuthProvider.ForceResendingToken) = apply { token = init() }

        fun build() = PhoneAuthDataModel(this)

    }
}

