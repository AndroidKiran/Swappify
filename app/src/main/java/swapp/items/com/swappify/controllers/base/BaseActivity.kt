package swapp.items.com.swappify.controllers.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection

abstract class BaseActivity<out B : ViewDataBinding, out V : BaseViewModel> : AppCompatActivity() {

    private lateinit var baseViewDataBinding: B

    private lateinit var baseViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
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
}
