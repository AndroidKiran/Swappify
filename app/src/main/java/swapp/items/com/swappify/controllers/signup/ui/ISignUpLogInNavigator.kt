package swapp.items.com.swappify.controllers.signup.ui

interface ISignUpLogInNavigator {

    fun openCountryCodeDialog()

    fun onNextClick()

    fun onResendClick()

    fun startHomeActivity()

    fun seekSmsReadPermission()
}