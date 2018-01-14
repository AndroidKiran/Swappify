package swapp.items.com.swappify.common

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AppUtils {

    companion object {

        val format = SimpleDateFormat("MMM dd yyyy", Locale.US)


        fun loadJSONFromAsset(context: Application?, assetPath: String?): String {

            try {
                val asset = context?.assets?.open(assetPath)
                val size = asset?.available()

                val buffer = ByteArray(size!!)
                asset.read(buffer)
                asset.close()

                return String(buffer)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return ""
        }

        fun getLocale(context: Context): Locale {
            val locale: Locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.resources.configuration.locales.get(0)
            } else {
                locale = context.resources.configuration.locale
            }
            return locale
        }

        fun isValidPhone(mobileNumber: String?, countryCode: String?): Boolean {

            if (mobileNumber == null || countryCode == null) {
                return false
            }

            val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
            val phoneNumber: Phonenumber.PhoneNumber
            try {
                phoneNumber = phoneUtil.parse(countryCode.plus(mobileNumber), countryCode)
            } catch (e: NumberParseException) {
                return false
            }

            if (phoneUtil.isValidNumber(phoneNumber)) {
                val numberType = phoneUtil.getNumberType(phoneNumber)
                if (numberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE
                        || numberType == PhoneNumberUtil.PhoneNumberType.MOBILE) {
                    return true
                }
            }

            return phoneNumber.countryCode == 91 && phoneNumber.nationalNumber.toString()
                    .matches("[789]\\d{9}".toRegex())
        }

        fun isEmpty(string: String?): Boolean = string == null || string.isEmpty()

        fun isNotEmpty(string: String?): Boolean = string != null && !TextUtils.isEmpty(string)

        fun toMMMddyyyy(time: Long?): String = format.format(Date(time!!))
    }
}