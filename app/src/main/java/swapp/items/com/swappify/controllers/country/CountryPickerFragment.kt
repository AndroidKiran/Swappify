package swapp.items.com.swappify.controllers.country

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.R
import swapp.items.com.swappify.components.MultiStateView
import swapp.items.com.swappify.controllers.base.BaseAndroidDialogFragment
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.controllers.country.viewmodel.CountryPickerViewModel
import swapp.items.com.swappify.databinding.FragmentCountryBinding
import javax.inject.Inject

class CountryPickerFragment : BaseAndroidDialogFragment<FragmentCountryBinding, CountryPickerViewModel>(), CountryPickerNavigator, View.OnClickListener {


    @Inject lateinit var countryAdapter: CountryAdapter<CountryPickerNavigator>

    @Inject lateinit var countryPickerViewModel: CountryPickerViewModel

    @Inject lateinit var layoutManager: LinearLayoutManager

    private lateinit var fragmentCountryBinding: FragmentCountryBinding

    companion object {
        val TAG : String = CountryPickerFragment.javaClass.simpleName

        fun newInstance(bundle: Bundle): CountryPickerFragment {
            val fragment = CountryPickerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countryAdapter.navigator = this
        countryPickerViewModel.baseNavigator = this
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCountryBinding = viewDataBinding
        initRecyclerView()
    }


    private fun initRecyclerView() {
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentCountryBinding.recyclerView.layoutManager = layoutManager
        fragmentCountryBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        fragmentCountryBinding.recyclerView.adapter = countryAdapter

        //One method one responsibility.May be we can move fetchBlogs() outside this function.
        countryPickerViewModel.fetchCountries()
    }

    override fun getViewModel(): CountryPickerViewModel = countryPickerViewModel

    override fun getBindingVariable(): Int = BR.androidViewModel

    override fun getLayoutId(): Int = R.layout.fragment_country


    override fun update(countries: Countries) {
        if (!countries.countries.isNotEmpty()) {
            countryAdapter.setData(countries.countries)
            fragmentCountryBinding.multiStateView.viewState = MultiStateView.VIEW_STATE_CONTENT
        } else {
            fragmentCountryBinding.multiStateView.viewState = MultiStateView.VIEW_STATE_ERROR
        }
    }

    override fun onCancel() {
        dismissDialog(TAG)
    }

    override fun onSelect(country: Countries.Country) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(p0: View?) {
        println("retry clicked")
    }
}