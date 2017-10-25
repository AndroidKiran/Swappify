package swapp.items.com.swappify.controllers.country

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.ObservableArrayList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.MultiStateView
import swapp.items.com.swappify.controllers.base.BaseAndroidDialogFragment
import swapp.items.com.swappify.controllers.base.FragmentCallback
import swapp.items.com.swappify.controllers.configs.EmptyViewConfiguration
import swapp.items.com.swappify.controllers.configs.ErrorViewConfiguration
import swapp.items.com.swappify.controllers.configs.RecyclerViewConfiguration
import swapp.items.com.swappify.controllers.configs.ToolbarConfiguration
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.databinding.FragmentCountryBinding
import javax.inject.Inject

class CountryPickerFragment : BaseAndroidDialogFragment<FragmentCountryBinding, CountryPickerViewModel>(), CountryPickerNavigator {


    @Inject lateinit var countryAdapter: CountryAdapter<CountryPickerNavigator>

    @Inject lateinit var layoutManager: LinearLayoutManager

    @Inject lateinit var viewFactory: ViewModelProvider.Factory

    @Inject lateinit var countryPickerViewModel: CountryPickerViewModel

    lateinit var toolbarConfiguration: ToolbarConfiguration

    lateinit var recyclerViewConfiguration: RecyclerViewConfiguration

    lateinit var emptyViewConfiguration: EmptyViewConfiguration

    lateinit var errorViewConfiguration: ErrorViewConfiguration

    private var fragmentCountryPickerBinding: FragmentCountryBinding? = null

    private var mListener: FragmentCallback? = null


    companion object {
        val TAG: String = CountryPickerFragment.javaClass.simpleName

        fun newInstance(bundle: Bundle?): CountryPickerFragment {
            val fragment = CountryPickerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emptyViewConfiguration = EmptyViewConfiguration()
        errorViewConfiguration = ErrorViewConfiguration()
        recyclerViewConfiguration = RecyclerViewConfiguration()
        toolbarConfiguration = ToolbarConfiguration()
        countryAdapter.setBaseNavigator(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerView()
        initEmptyView(ContextCompat.getDrawable(context, R.mipmap.ic_launcher),
                getString(R.string.str_no_countries))

        initErrorView(getString(R.string.str_no_countries),
                ContextCompat.getDrawable(context, R.mipmap.ic_launcher))

        setRetryListener()

        countryPickerViewModel.setBaseNavigator(this)
        countryPickerViewModel.fetchCountries()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as FragmentCallback
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    private fun initToolbar() {
        val title: String = getString(R.string.str_choose_country)
        val titleColor: Int = ContextCompat.getColor(context, R.color.white)
        val navigationIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.vc_cross_white)
        toolbarConfiguration.setToolbarConfig(title, titleColor,
                View.OnClickListener { onNavigationBackClickListener() }, navigationIcon)
    }

    private fun initRecyclerView() {
        recyclerViewConfiguration.setRecyclerConfig(layoutManager, countryAdapter)
    }

    private fun initEmptyView(emptyDrawable: Drawable, emptyMsg: String) {
        emptyViewConfiguration.setEmptyViewConfig(emptyDrawable, emptyMsg)
    }

    private fun initErrorView(errorMsg: String, errorDrawable: Drawable) {
        errorViewConfiguration.setErrorViewConfig(errorDrawable, errorMsg)
    }

    override fun getViewModel(): CountryPickerViewModel {
        countryPickerViewModel = ViewModelProviders.of(this, viewFactory).get(countryPickerViewModel::class.java)
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
    }

    override fun updateAdapter() {
        val countries: ObservableArrayList<Countries.Country>? = countryPickerViewModel.countriesLiveData
        if (countries!!.isNotEmpty()) {
            fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.viewState = MultiStateView.VIEW_STATE_CONTENT
            countryAdapter.setData(countries)
        } else {
            fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.viewState = MultiStateView.VIEW_STATE_ERROR
        }
    }

    private fun setRetryListener() {
        errorViewConfiguration.setErrorRetryListener(View.OnClickListener { onRetryClickListener() })
    }

    private fun onNavigationBackClickListener() {
        dismissDialog(TAG)
    }

    private fun onRetryClickListener() {
        countryPickerViewModel.fetchCountries()
    }

    override fun onSelect(country: Countries.Country?) {
        dismissDialog(TAG)
        val bundle = Bundle()
        bundle.putParcelable(COUNTRY_EXTRA, country)
        mListener?.onFragmentInteraction(bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}