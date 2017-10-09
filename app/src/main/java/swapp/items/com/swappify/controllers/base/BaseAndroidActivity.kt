package swapp.items.com.swappify.controllers.base

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection

abstract class BaseAndroidActivity<out B, out V> : AppCompatActivity(), FragmentCallback  where B : ViewDataBinding, V : BaseAndroidViewModel<*> {

    private lateinit var baseViewDataBinding: B
    val viewDataBinding: B
        get() = baseViewDataBinding

    private lateinit var baseViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDependencyInjection()
        performDataBinding()
        baseViewModel.onViewCreated()
    }

    private fun performDataBinding() {
        baseViewDataBinding = DataBindingUtil.setContentView<B>(this, getLayoutId())
        baseViewModel = getViewModel()
        baseViewDataBinding.setVariable(getBindingVariable(), baseViewModel)
        baseViewDataBinding.executePendingBindings()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

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

    abstract fun getBindingVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }
}