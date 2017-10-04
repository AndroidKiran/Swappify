package swapp.items.com.swappify.base

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

abstract class BaseFragment<out B, out V> : Fragment() where B : ViewDataBinding, V : BaseViewModel<*> {

    private lateinit var mActivity: BaseActivity<*, *>
    private lateinit var mViewDataBinding: B
    private lateinit var mViewModel: V
    private lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate<B>(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = getViewModel()
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
        mViewModel.onViewCreated()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.mActivity = context
            mActivity.onFragmentAttached()
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun getBaseActivity(): BaseActivity<*, *> = mActivity

    fun getViewDataBinding(): B = mViewDataBinding


    override fun onDestroyView() {
        mViewModel.onDestroyView()
        super.onDestroyView()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int
}