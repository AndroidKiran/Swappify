package swapp.items.com.swappify.controllers.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<out B : ViewDataBinding, out V : BaseViewModel> : Fragment() {

    private lateinit var baseViewDataBinding: B

    private lateinit var baseViewModel: V
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
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
    }

    override fun onDestroyView() {
        baseViewModel.onDestroyView()
        super.onDestroyView()
    }


    fun getViewDataBinding(): B = baseViewDataBinding


    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    abstract fun executePendingVariablesBinding()

    abstract fun getViewModel(): V

    @LayoutRes abstract fun getLayoutId(): Int
}