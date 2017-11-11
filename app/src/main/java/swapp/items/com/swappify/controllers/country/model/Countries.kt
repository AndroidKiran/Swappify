package swapp.items.com.swappify.controllers.country.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Countries {

    companion object {
        const val COUNTRY_EXTRA: String = "country_extra"
    }

    @SerializedName("countries")
    val countries = ArrayList<Country>()


    data class Country constructor(@SerializedName("name") val name: String,
                                   @SerializedName("isoCode") val isoCode: String,
                                   @SerializedName("code") val code: String) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(isoCode)
            parcel.writeString(code)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<Country> {
            override fun createFromParcel(parcel: Parcel): Country = Country(parcel)

            override fun newArray(size: Int): Array<Country?> = arrayOfNulls(size)
        }
    }
}