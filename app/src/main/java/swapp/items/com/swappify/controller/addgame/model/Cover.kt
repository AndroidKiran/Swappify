package swapp.items.com.swappify.controller.addgame.model

import android.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Cover constructor(
        @SerializedName("url")
        var url: String?) : BaseObservable(), PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelCover.CREATOR
    }


}