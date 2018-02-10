package swapp.items.com.swappify.repo.user.model

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User @JvmOverloads constructor(var userID: String?,
                                          var userNumber: String?,
                                          var userName: String? = "",
                                          var userPic: String? = "",
                                          var geoPoint: GeoPoint? = null,
                                          @ServerTimestamp
                                          var createdAt: Date? = null,
                                          @ServerTimestamp
                                          var modifiedAt: Date? = null) {

    constructor() : this("", "", "", "", null)


}
