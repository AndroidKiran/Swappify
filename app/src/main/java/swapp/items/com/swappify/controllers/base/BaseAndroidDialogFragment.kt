package swapp.items.com.swappify.controllers.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection

abstract class BaseAndroidDialogFragment<B, out V> : DialogFragment() where B : ViewDataBinding, V : BaseAndroidViewModel<*> {

    private lateinit var baseViewDataBinding: B

    private lateinit var baseViewModel: V
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDependencyInjection()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        baseViewDataBinding = DataBindingUtil.inflate<B>(inflater, getLayoutId(), container, false)
        rootView = baseViewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseViewModel = getViewModel()
        executePendingVariablesBinding()
        baseViewModel.onViewCreated()
        baseViewDataBinding.executePendingBindings()
    }

    override fun onDestroyView() {
        baseViewModel.onDestroyView()
        super.onDestroyView()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun executePendingVariablesBinding()

    fun getViewDataBinding(): B? = baseViewDataBinding


    override fun show(fragmentManager: FragmentManager, tag: String) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    fun dismissDialog(tag: String) {
        dialog.dismiss()
    }
}