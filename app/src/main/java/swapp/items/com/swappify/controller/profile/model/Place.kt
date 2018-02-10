package swapp.items.com.swappify.controller.profile.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Place @JvmOverloads constructor(val id: String,
                                           val name:CharSequence?,
                                           val address:CharSequence?,
                                           val latitude: Double?,
                                           val longitude: Double?): PaperParcelable {

    constructor():this("","","",0.0,0.0)

    companion object {
        @JvmField
        val CREATOR = PaperParcelPlace.CREATOR
    }

}
