package swapp.items.com.swappify.controllers.country.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Country constructor(@SerializedName("name")
                               @get:Bindable
                               val name: String,
                               @SerializedName("isoCode")
                               @get:Bindable
                               val isoCode: String,
                               @SerializedName("code") val code: String) : BaseObservable(), PaperParcelable {


    companion object {
        @JvmField
        val CREATOR = PaperParcelCountry.CREATOR
    }
}