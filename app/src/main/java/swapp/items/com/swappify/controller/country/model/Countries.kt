package swapp.items.com.swappify.controller.country.model


import com.google.gson.annotations.SerializedName

class Countries {

    companion object {
        const val COUNTRY_EXTRA: String = "country_extra"
    }

    @SerializedName("countries")
    val countries = ArrayList<Country>()


}