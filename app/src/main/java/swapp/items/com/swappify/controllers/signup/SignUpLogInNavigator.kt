package swapp.items.com.swappify.controllers.signup

import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel

interface SignUpLogInNavigator {

    fun openCountryCodeDialog()

    fun verifyPhoneNumber()

    fun handleOnSucces(phoneAuthDataModel: PhoneAuthDataModel)

    fun handleOnError(error: Throwable)
}