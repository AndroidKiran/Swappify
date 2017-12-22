package swapp.items.com.swappify.controllers.country.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.MultiStateView
import swapp.items.com.swappify.controllers.base.BaseDialogFragment
import swapp.items.com.swappify.controllers.base.IFragmentCallback
import swapp.items.com.swappify.controllers.configs.*
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.model.Countries.Companion.COUNTRY_EXTRA
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.databinding.FragmentCountryBinding
import javax.inject.Inject

class CountryPickerFragment : BaseDialogFragment<FragmentCountryBinding, CountryPickerViewModel>(),
        ICountryPickerNavigator {


    @Inject lateinit var countryAdapter: CountryAdapter<ICountryPickerNavigator>

    @Inject lateinit var layoutManager: LinearLayoutManager

    @Inject lateinit var viewFactory: ViewModelProvider.Factory

    @Inject lateinit var countryPickerViewModel: CountryPickerViewModel

    lateinit var toolbarConfiguration: ToolbarConfiguration

    lateinit var recyclerViewConfiguration: RecyclerViewConfiguration

    lateinit var emptyViewConfiguration: EmptyViewConfiguration

    lateinit var errorViewConfiguration: ErrorViewConfiguration

    lateinit var contentLoadingConfiguration: ContentLoadingConfiguration

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
        emptyViewConfiguration = EmptyViewConfiguration()
        errorViewConfiguration = ErrorViewConfiguration()
        recyclerViewConfiguration = RecyclerViewConfiguration()
        toolbarConfiguration = ToolbarConfiguration()
        contentLoadingConfiguration = ContentLoadingConfiguration()
        contentLoadingConfiguration.setConfig(getString(R.string.msg_loading))
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

        countryPickerViewModel.fetchCountries()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as IFragmentCallback
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    private fun initToolbar() {
        val title: String = getString(R.string.str_choose_country)
        val titleColor: Int = ContextCompat.getColor(context, R.color.white)
        val navigationIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.vc_cross_white)
        toolbarConfiguration.setToolbarConfig(title, titleColor, toolbarClickListener, navigationIcon)
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
        countryPickerViewModel.getCountriesLiveData().observe(this@CountryPickerFragment,
                Observer<ArrayList<Countries.Country>?> { countryList: ArrayList<Countries.Country>? ->
                    if (countryList!!.isNotEmpty()) {
                        countryAdapter.setData(countryList)
                        fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.viewState =
                                MultiStateView.VIEW_STATE_CONTENT
                    } else {
                        fragmentCountryPickerBinding?.multiStateViewLayout?.multiStateView?.viewState =
                                MultiStateView.VIEW_STATE_ERROR
                    }
                })
    }

    private fun setRetryListener() {
        errorViewConfiguration.setErrorRetryListener(View.OnClickListener { onRetryClickListener() })
    }

    private fun onRetryClickListener() {
        countryPickerViewModel.fetchCountries()
    }

    override fun onSelect(country: Countries.Country?) {
        dismissDialog()
        val bundle = Bundle()
        bundle.putParcelable(COUNTRY_EXTRA, country)
        mListener?.onFragmentInteraction(bundle)
    }

    private val toolbarClickListener = View.OnClickListener {
        dismissDialog()
    }
}