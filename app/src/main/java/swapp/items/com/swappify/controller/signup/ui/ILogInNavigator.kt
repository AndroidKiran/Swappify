package swapp.items.com.swappify.controller.signup.ui

import android.text.Editable

interface ILogInNavigator {

    fun onClickCountryCode()

    fun onClickNext()

    fun onClickResendOtp()

    fun afterMobileNumChanged(editable: Editable)

    fun afterOtpChanged(editable: Editable)
}