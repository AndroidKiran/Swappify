package swapp.items.com.swappify.controller.signup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import java.util.regex.Matcher
import java.util.regex.Pattern

class SMSReceiver constructor(private val smsReceivedListener: SMSReceivedListener?): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val extras: Bundle? = intent?.extras?: return
            val pdus = extras?.get(PDUS) as Array<*>
            var code = ""

            for (i in pdus.indices) {
                val message: SmsMessage? = getIncomingMessage(pdus[i], extras)

                val smsBody: String? = message?.messageBody
                if (smsBody!!.contains(SMS_STRING_PATTERN)) {
                    val p: Pattern? = Pattern.compile(REGEX)
                    val m: Matcher? = p?.matcher(smsBody)
                    while (m!!.find()) {
                        code = m.group()
                    }
                }
                val bundle = Bundle()
                bundle.putString(VERIFICATION_CODE, code)
                smsReceivedListener?.onSMSReceived(bundle)

            }
        } catch (e: Exception) {

        }
    }

    private fun getIncomingMessage(aObject: Any?, bundle: Bundle?): SmsMessage? {
        val currentSMS: SmsMessage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle?.getString(FORMAT)
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray, format)
        } else {
            currentSMS = SmsMessage.createFromPdu(aObject as ByteArray)
        }
        return currentSMS
    }

    interface SMSReceivedListener {
        fun onSMSReceived(bundle: Bundle?)
    }

    companion object {
        const val VERIFICATION_CODE: String = "verification_code"
        const val PDUS = "pdus"
        const val SMS_STRING_PATTERN = "is your verification code."
        const val FORMAT = "format"
        const val REGEX = "\\d+"
    }
}
