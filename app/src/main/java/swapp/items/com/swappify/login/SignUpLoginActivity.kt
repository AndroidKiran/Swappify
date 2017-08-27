package swapp.items.com.swappify.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R


class SignUpLoginActivity : AppCompatActivity(), SignUpActionListener{

    companion object {
        val ACTION_SIGNUP : String = BuildConfig.APPLICATION_ID + ".action" +".SIGNUP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_loader_only)

        if (null == savedInstanceState) {
            loadValidatePhoneNumFragment()
        }

    }


    private fun loadValidatePhoneNumFragment(){
        PhoneNumValidationFragment.newInstance(supportFragmentManager, null, R.id.container)
    }

    private fun loadValidateSmsFragment() {
        OtpVerificationFragment.newInstance(supportFragmentManager, null, R.id.container)
    }


    override fun onOtpVerificationButtonClicked() {
        println("OnVerifiy clicked")
    }

    override fun onNextButtonClicked() {
        loadValidateSmsFragment()
    }
}