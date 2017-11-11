package swapp.items.com.swappify.controllers.signup.ui

interface SignUpLogInNavigator {

    fun openCountryCodeDialog()

    fun onNextClick()

    fun onResendClick()

    fun onVerifyOtpClick()

    fun startHomeActivity()

    fun seekSmsReadPermission()
}