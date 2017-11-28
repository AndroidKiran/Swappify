package swapp.items.com.swappify.components.search

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable

data class SearchItem constructor(@get:Bindable val text: String,
                                  @get:Bindable val iconUrl: String,
                                  @get:Bindable val placeHolder: Int) : BaseObservable(), Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(iconUrl)
        parcel.writeInt(placeHolder)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SearchItem> {
        override fun createFromParcel(parcel: Parcel): SearchItem = SearchItem(parcel)

        override fun newArray(size: Int): Array<SearchItem?> = arrayOfNulls(size)
    }

}
