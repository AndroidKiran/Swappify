package swapp.items.com.swappify.base

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
import swapp.items.com.swappify.base.BaseFragment.Callback

abstract class BaseActivity<out B, out V> : AppCompatActivity(), Callback  where B : ViewDataBinding, V : BaseViewModel<*> {

    private lateinit var mViewDataBinding: B
    private lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDependencyInjection()
        performDataBinding()
        mViewModel.onViewCreated()
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView<B>(this, getLayoutId())
        mViewModel = getViewModel()
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
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
        mViewModel.onDestroyView()
        super.onDestroy()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun getViewDataBinding(): B = mViewDataBinding

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