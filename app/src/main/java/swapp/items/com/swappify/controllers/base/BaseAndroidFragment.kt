package swapp.items.com.swappify.controllers.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection

abstract class BaseAndroidFragment<out B, out V> : Fragment() where B : ViewDataBinding, V : BaseAndroidViewModel<*> {

    private lateinit var baseActivity: FragmentCallback
    private lateinit var baseViewDataBinding: B
    val viewDataBinding: B
        get() = baseViewDataBinding

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
        baseViewDataBinding.setVariable(getBindingVariable(), baseViewModel)
        baseViewDataBinding.executePendingBindings()
        baseViewModel.onViewCreated()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FragmentCallback) {
            val activity = context as FragmentCallback
            this.baseActivity = activity
            activity.onFragmentAttached()
        }
    }

    override fun onDetach() {
        super.onDetach()
    }


    override fun onDestroyView() {
        baseViewModel.onDestroyView()
        super.onDestroyView()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    abstract fun getViewModel(): V

    abstract fun getBindingVariable(): Int

    @LayoutRes
    abstract fun getLayoutId(): Int
}