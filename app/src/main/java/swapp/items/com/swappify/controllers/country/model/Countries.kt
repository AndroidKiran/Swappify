package swapp.items.com.swappify.controllers.country.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Countries {

    @SerializedName("countries")
    val countries = listOf<Country>()


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

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Country> {
            override fun createFromParcel(parcel: Parcel): Country {
                return Country(parcel)
            }

            override fun newArray(size: Int): Array<Country?> {
                return arrayOfNulls(size)
            }
        }
    }
}