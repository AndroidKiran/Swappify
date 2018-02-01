package swapp.items.com.swappify.repo.user.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class User @JvmOverloads constructor(var userID: String?,
                                              var userNumber: String?,
                                              var userName: String? = "",
                                              var userLocation: String? = "",
                                              var userPic: String? = "") : PaperParcelable {

    constructor():this("", "", "", "", "")

    companion object {
        @JvmField
        val CREATOR = PaperParcelUser.CREATOR
    }

}
