package swapp.items.com.swappify.controller.base

import android.content.BroadcastReceiver
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.observe
import swapp.items.com.swappify.common.extension.showNoNetworkSnackBar
import swapp.items.com.swappify.common.extension.showSnackBar
import swapp.items.com.swappify.controller.configs.SnackbarConfiguration
import swapp.items.com.swappify.mvvm.NetworkConnectionLifeCycleObserver
import javax.inject.Inject

abstract class BaseActivity<out B : ViewDataBinding, out V : BaseViewModel> : AppCompatActivity(), HasBroadcastReceiverInjector {

    @Inject
    lateinit var broadcastReceiverDispatchingAndroidInjector: DispatchingAndroidInjector<BroadcastReceiver>

    private lateinit var baseViewDataBinding: B

    private lateinit var baseViewModel: V

    val snackBarConfiguration = SnackbarConfiguration()

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
        NetworkConnectionLifeCycleObserver(lifecycle, baseViewModel.isNetConnected, this@BaseActivity)
        observerNetworkChange()
        observerApiCallErrorChange()
    }

    private fun performDataBinding() {
        baseViewDataBinding = DataBindingUtil.setContentView(this@BaseActivity, getLayoutId())
        baseViewModel = getViewModel()
        executePendingVariablesBinding()
        baseViewModel.onViewCreated()
        baseViewDataBinding.executePendingBindings()
    }

    override fun onDestroy() {
        baseViewModel.onDestroyView()
        super.onDestroy()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun executePendingVariablesBinding()

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    fun getViewDataBinding(): B = baseViewDataBinding


    private fun observerNetworkChange() = baseViewModel.isNetConnected.observe(this@BaseActivity) {
        if (it == false) {
            baseViewModel.isSnackBarAlive = true
            hideKeyboard()
            snackBarConfiguration.showNoNetworkSnackBar(getString(R.string.str_no_internet_title),
                    getString(R.string.str_dismiss), View.OnClickListener { })
        } else {
            if (baseViewModel.isSnackBarAlive) {
                snackBarConfiguration.showSnackBar(getString(R.string.str_internet_title), SnackbarConfiguration.Type.VALID)
            }
        }
    }

    private fun observerApiCallErrorChange() = baseViewModel.apiError.observe(this@BaseActivity) {
        if (it == true) {
            hideKeyboard()
            snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg), SnackbarConfiguration.Type.NEUTRAL)
        }
    }

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverDispatchingAndroidInjector

}
