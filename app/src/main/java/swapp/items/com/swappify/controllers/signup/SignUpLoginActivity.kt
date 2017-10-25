package swapp.items.com.swappify.controllers.signup

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.BuildConfig
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.TextChangeListener
import swapp.items.com.swappify.controllers.base.BaseActivity
import swapp.items.com.swappify.controllers.base.FragmentCallback
import swapp.items.com.swappify.controllers.configs.EditTextConfiguration
import swapp.items.com.swappify.controllers.country.CountryPickerFragment
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controllers.signup.model.PhoneAuthDataModel
import swapp.items.com.swappify.controllers.signup.viewModel.SignUpLogInViewModel
import swapp.items.com.swappify.databinding.ActivitySignupBinding
import swapp.items.com.swappify.utils.AppUtils.Companion.getLocale
import javax.inject.Inject


class SignUpLoginActivity : BaseActivity<ActivitySignupBinding, SignUpLogInViewModel>(), SignUpLogInNavigator, FragmentCallback, HasSupportFragmentInjector, TextChangeListener.OnTextChangeListener {


    companion object {
        val ACTION_SIGNUP: String = BuildConfig.APPLICATION_ID + ".action" + ".SIGNUP"
    }

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var signUpLogInViewModel: SignUpLogInViewModel

    private lateinit var activitySignupBinding: ActivitySignupBinding

    private var editTextConfiguration: EditTextConfiguration = EditTextConfiguration()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpLogInViewModel.baseNavigator = this
        initViews()
        initTwoWayBindings()
    }


    private fun initViews() {
        prefetchCountryCode()
    }

    private fun initTwoWayBindings() {
        val textChangeListener: TextChangeListener? = TextChangeListener(
                TextChangeListener.TextChangerType.LOGIN_PHONE_NUM, this@SignUpLoginActivity)
        editTextConfiguration.setEditTextConfig(textChangeListener)
    }

    private fun prefetchCountryCode() {
        val countryISOCode: String = getLocale(this).country
        val code: Int = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryISOCode)
        var countryCode: String = "+".plus(code)
        if (code == 0) {
            countryCode = "+91"
        }
        signUpLogInViewModel.countryCode.set(countryCode)
    }


    override fun getLayoutId(): Int = R.layout.activity_signup

    override fun getViewModel(): SignUpLogInViewModel {
        signUpLogInViewModel = ViewModelProviders.of(this, viewFactory).get(SignUpLogInViewModel::class.java)
        return signUpLogInViewModel
    }


    override fun executePendingVariablesBinding() {
        activitySignupBinding = getViewDataBinding()
        activitySignupBinding.setVariable(BR.viewModel, signUpLogInViewModel)
        activitySignupBinding.setVariable(BR.editTextConfig, editTextConfiguration)
    }

    override fun verifyPhoneNumber() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleOnSucces(phoneAuthDataModel: PhoneAuthDataModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun handleOnError(error: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openCountryCodeDialog() {
        CountryPickerFragment.newInstance(Bundle()).show(supportFragmentManager, CountryPickerFragment.TAG)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onFragmentInteraction(bundle: Bundle?) {
        val country: Countries.Country? = bundle?.getParcelable(COUNTRY_EXTRA)
        signUpLogInViewModel.selectedCountry.set(country)
        signUpLogInViewModel.countryCode.set(country?.isoCode)
    }

    override fun onNextClick(phoneNumber: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun beforeTextChanged(textChangerType: TextChangeListener.TextChangerType?, charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTextChanged(textChangerType: TextChangeListener.TextChangerType?, charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterTextChanged(textChangerType: TextChangeListener.TextChangerType?, editable: Editable?) {
        if (textChangerType == TextChangeListener.TextChangerType.LOGIN_PHONE_NUM) {
            var phoneNum: String = editable?.toString()!!
        }
    }



}