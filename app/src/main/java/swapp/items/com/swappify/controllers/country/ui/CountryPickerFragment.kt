package swapp.items.com.swappify.controllers.country.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.common.extension.*
import swapp.items.com.swappify.components.BindedMultiStateView
import swapp.items.com.swappify.controllers.base.BaseDialogFragment
import swapp.items.com.swappify.controllers.base.IFragmentCallback
import swapp.items.com.swappify.controllers.configs.*
import swapp.items.com.swappify.controllers.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controllers.country.model.Country
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.databinding.FragmentCountryBinding
import javax.inject.Inject

class CountryPickerFragment : BaseDialogFragment<FragmentCountryBinding, CountryPickerViewModel>(),
        CountryAdapter.CountryItemViewModelListener {


    @Inject lateinit var countryAdapter: CountryAdapter<CountryAdapter.CountryItemViewModelListener>

    @Inject lateinit var layoutManager: LinearLayoutManager

    @Inject lateinit var viewFactory: ViewModelProvider.Factory

    @Inject lateinit var countryPickerViewModel: CountryPickerViewModel

    private val recyclerViewConfiguration = RecyclerViewConfiguration()

    private val toolbarConfiguration = ToolbarConfiguration()

    private val emptyViewConfiguration = EmptyViewConfiguration()

    private val errorViewConfiguration = ErrorViewConfiguration()

    private val contentLoadingConfiguration = ContentLoadingConfiguration()

    private var fragmentCountryPickerBinding: FragmentCountryBinding? = null

    private var mListener: IFragmentCallback? = null


    companion object {
        val TAG: String = CountryPickerFragment::class.java.simpleName

        fun newInstance(bundle: Bundle?): CountryPickerFragment {
            val fragment = CountryPickerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryAdapter.navigator = this
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {

        toolbarConfiguration.toolbarBinding(getString(R.string.str_choose_country),
                ContextCompat.getColor(context, R.color.white), 0, toolbarClickListener,
                ContextCompat.getDrawable(context, R.drawable.vc_cross_white), null)

        emptyViewConfiguration.emptyViewBinding(getString(R.string.str_no_countries),
                ContextCompat.getDrawable(context, R.mipmap.ic_launcher))

        errorViewConfiguration.errorViewBinding(getString(R.string.str_no_countries),
                ContextCompat.getDrawable(context, R.mipmap.ic_launcher), onRetryClickListener)

        contentLoadingConfiguration.contentLoadingBinding(getString(R.string.msg_loading))

        recyclerViewConfiguration.recyclerViewBinding(countryAdapter, layoutManager)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as IFragmentCallback
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    override fun getViewModel(): CountryPickerViewModel {
        countryPickerViewModel = ViewModelProviders.of(this@CountryPickerFragment,
                viewFactory).get(countryPickerViewModel::class.java)
        return countryPickerViewModel
    }

    override fun getLayoutId(): Int = R.layout.fragment_country

    override fun executePendingVariablesBinding() {
        fragmentCountryPickerBinding = getViewDataBinding()
        fragmentCountryPickerBinding?.setVariable(BR.androidViewModel, countryPickerViewModel)
        fragmentCountryPickerBinding?.setVariable(BR.toolbarConfig, toolbarConfiguration)
        fragmentCountryPickerBinding?.setVariable(BR.recyclerViewConfig, recyclerViewConfiguration)
        fragmentCountryPickerBinding?.setVariable(BR.errorViewConfig, errorViewConfiguration)
        fragmentCountryPickerBinding?.setVariable(BR.emptyViewConfig, emptyViewConfiguration)
        fragmentCountryPickerBinding?.setVariable(BR.contentLoadingViewConfig, contentLoadingConfiguration)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observerLiveData()
    }

    private fun observerLiveData() {
        countryPickerViewModel.countriesLiveData.observe(this@CountryPickerFragment) {
            if (it!!.isNotEmpty()) {
                countryAdapter.setData(it)
                fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_CONTENT)
            } else {
                fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_ERROR)
            }
        }
    }

    private val onRetryClickListener = View.OnClickListener {
        countryPickerViewModel.reload.value = true
        fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_LOADING)
    }

    override fun onItemClick(country: Country?) {
        dismissDialog()
        val bundle = Bundle()
        bundle.putParcelable(COUNTRY_EXTRA, country)
        mListener?.onFragmentInteraction(bundle)
    }

    private val toolbarClickListener = View.OnClickListener {
        dismissDialog()
    }
}