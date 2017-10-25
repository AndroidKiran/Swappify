package swapp.items.com.swappify.controllers.signup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import swapp.items.com.swappify.controllers.country.model.Countries
import swapp.items.com.swappify.databinding.ItemCountryBinding

class CountryAdapter : BaseAdapter(){

    var countriesList: ArrayList<Countries.Country>? = arrayListOf()

    fun setData(countries: List<Countries.Country>) {
        countriesList?.addAll(countries)
        this.notifyDataSetChanged()
    }


    override fun getView(position: Int, convertedView: View?, parent: ViewGroup?): View {
        var countryViewHolder: CountryViewHolder?
        if (convertedView == null) {
            val itemCountryViewBinding = ItemCountryBinding.inflate(LayoutInflater.from(parent?.context),
                    parent, false)
            countryViewHolder = CountryViewHolder(itemCountryViewBinding)
            convertedView?.tag = countryViewHolder
        } else {
            countryViewHolder = convertedView.tag as CountryViewHolder
        }
        countryViewHolder.onBind(position)
        return convertedView!!
    }

    override fun getItem(position: Int): Any = countriesList!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = countriesList!!.size

    inner class CountryViewHolder constructor(val binding: ItemCountryBinding) {

        fun onBind(position: Int) {
            val country: Countries.Country = countriesList!!.get(position)
//            binding.itemViewModel = CountryItemViewModel(
//                    country = country
//            )
            binding.executePendingBindings()
        }

    }
}