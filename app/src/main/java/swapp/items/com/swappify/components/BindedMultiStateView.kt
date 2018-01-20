package swapp.items.com.swappify.components

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.controller.configs.ContentLoadingConfiguration
import swapp.items.com.swappify.controller.configs.EmptyViewConfiguration
import swapp.items.com.swappify.controller.configs.ErrorViewConfiguration

class BindedMultiStateView<B : ViewDataBinding> : FrameLayout {

    companion object {
        const val VIEW_STATE_CONTENT = 0

        const val VIEW_STATE_ERROR = 1

        const val VIEW_STATE_EMPTY = 2

        const val VIEW_STATE_LOADING = 3
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(VIEW_STATE_CONTENT.toLong(), VIEW_STATE_ERROR.toLong(), VIEW_STATE_EMPTY.toLong(), VIEW_STATE_LOADING.toLong())
    annotation class ViewState

    @ViewState
    private var viewState = VIEW_STATE_CONTENT

    private lateinit var loadingViewBinding: ViewDataBinding
    private lateinit var emptyViewBinding: ViewDataBinding
    private lateinit var errorViewBinding: ViewDataBinding

    private var inflater: LayoutInflater? = null
    private var contentView: View? = null
    private var loadingView: View? = null
    private var emptyView: View? = null
    private var errorView: View? = null


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        inflater = LayoutInflater.from(context)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView)

        val loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1)
        if (loadingViewResId > -1) {
            loadingView = DataBindingUtil.inflate<B>(inflater, loadingViewResId, this, false).also {
                loadingViewBinding = it
            }.root

            addView(loadingView, loadingView?.layoutParams)
        }

        val errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1)
        if (errorViewResId > -1) {
            errorView = DataBindingUtil.inflate<B>(inflater, errorViewResId, this, false).also {
                errorViewBinding = it
            }.root

            addView(errorView, errorView?.layoutParams)
        }


        val emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1)
        if (emptyViewResId > -1) {
            emptyView = DataBindingUtil.inflate<B>(inflater, emptyViewResId, this, false).also {
                emptyViewBinding = it
            }.root

            addView(emptyView, emptyView?.layoutParams)
        }

        val contentState = a.getInt(R.styleable.MultiStateView_msv_viewState, VIEW_STATE_CONTENT)

        when (contentState) {
            VIEW_STATE_CONTENT -> viewState = VIEW_STATE_CONTENT

            VIEW_STATE_ERROR -> viewState = VIEW_STATE_ERROR

            VIEW_STATE_EMPTY -> viewState = VIEW_STATE_EMPTY

            VIEW_STATE_LOADING -> viewState = VIEW_STATE_LOADING
        }

        a.recycle()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (contentView == null) throw IllegalArgumentException("Content view is not defined")
        setView()
    }

    override fun addView(child: View?) {
        if (isValidContentView(child)) contentView = child
        super.addView(child)
    }

    override fun addView(child: View?, index: Int) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, index)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, index, params)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (isValidContentView(child)) contentView = child
        super.addView(child, width, height)
    }

    override fun addViewInLayout(child: View?, index: Int, params: ViewGroup.LayoutParams?): Boolean {
        if (isValidContentView(child)) contentView = child
        return super.addViewInLayout(child, index, params)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams, preventRequestLayout: Boolean): Boolean {
        if (isValidContentView(child)) contentView = child
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    private fun isValidContentView(view: View?): Boolean {
        return if (contentView != null && contentView != view) {
            false
        } else view != loadingView && view != errorView && view != emptyView

    }

    fun setViewState(@ViewState state: Int) {
        if (state != viewState) {
            viewState = state
            setView()
        }
    }

    private fun setView() {
        when (viewState) {
            VIEW_STATE_LOADING -> {
                if (loadingView == null) {
                    throw NullPointerException("Loading View")
                }

                loadingView?.visibility = View.VISIBLE
                if (contentView != null) contentView?.visibility = View.GONE
                if (errorView != null) errorView?.visibility = View.GONE
                if (emptyView != null) emptyView?.visibility = View.GONE
            }

            VIEW_STATE_EMPTY -> {
                if (emptyView == null) {
                    throw NullPointerException("Empty View")
                }

                emptyView?.visibility = View.VISIBLE
                if (loadingView != null) loadingView?.visibility = View.GONE
                if (errorView != null) errorView?.visibility = View.GONE
                if (contentView != null) contentView?.visibility = View.GONE
            }

            VIEW_STATE_ERROR -> {
                if (errorView == null) {
                    throw NullPointerException("Error View")
                }

                errorView?.visibility = View.VISIBLE
                if (loadingView != null) loadingView?.visibility = View.GONE
                if (contentView != null) contentView?.visibility = View.GONE
                if (emptyView != null) emptyView?.visibility = View.GONE
            }

            VIEW_STATE_CONTENT -> {
                if (contentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw NullPointerException("Content View")
                }

                contentView?.visibility = View.VISIBLE
                if (loadingView != null) loadingView?.visibility = View.GONE
                if (errorView != null) errorView?.visibility = View.GONE
                if (emptyView != null) emptyView?.visibility = View.GONE
            }
            else -> {
                if (contentView == null) {
                    throw NullPointerException("Content View")
                }
                contentView?.visibility = View.VISIBLE
                if (loadingView != null) loadingView?.visibility = View.GONE
                if (errorView != null) errorView?.setVisibility(View.GONE)
                if (emptyView != null) emptyView?.setVisibility(View.GONE)
            }
        }
    }

    @SuppressLint("SwitchIntDef")
    private fun setViewForState(view: View?, @ViewState state: Int, switchToState: Boolean) {
        when (state) {
            VIEW_STATE_LOADING -> {
                if (loadingView != null) removeView(loadingView)
                loadingView = view
                addView(loadingView)
            }

            VIEW_STATE_EMPTY -> {
                if (emptyView != null) removeView(emptyView)
                emptyView = view
                addView(emptyView)
            }

            VIEW_STATE_ERROR -> {
                if (errorView != null) removeView(errorView)
                errorView = view
                addView(errorView)
            }

            VIEW_STATE_CONTENT -> {
                if (contentView != null) removeView(contentView)
                contentView = view
                addView(contentView)
            }
        }

        if (switchToState) setViewState(state)
    }

    fun setViewForState(view: View, @ViewState state: Int) {
        setViewForState(view, state, false)
    }


    private fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int, switchToState: Boolean) {
        if (inflater == null) inflater = LayoutInflater.from(context)
        val view = inflater?.inflate(layoutRes, this, false)
        setViewForState(view, state, switchToState)
    }


    fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int) {
        setViewForState(layoutRes, state, false)
    }

    @SuppressLint("SwitchIntDef")
    fun getView(@ViewState state: Int): View? = when (state) {
        VIEW_STATE_LOADING -> loadingView

        VIEW_STATE_CONTENT -> contentView

        VIEW_STATE_EMPTY -> emptyView

        VIEW_STATE_ERROR -> errorView

        else -> null
    }

    @ViewState
    fun getViewState(): Int = viewState


    fun setEmptyViewConfiguration(emptyViewConfiguration: EmptyViewConfiguration?) {
        emptyViewBinding.setVariable(BR.emptyViewConfig, emptyViewConfiguration)
        emptyViewBinding.executePendingBindings()
    }

    fun setErrorViewConfiguration(errorViewConfiguration: ErrorViewConfiguration?) {
        errorViewBinding.setVariable(BR.errorViewConfig, errorViewConfiguration)
        errorViewBinding.executePendingBindings()
    }

    fun setContentLoadingViewConfiguration(contentLoadingViewConfiguration: ContentLoadingConfiguration?) {
        loadingViewBinding.setVariable(BR.contentLoadingViewConfig, contentLoadingViewConfiguration)
        loadingViewBinding.executePendingBindings()
    }

}