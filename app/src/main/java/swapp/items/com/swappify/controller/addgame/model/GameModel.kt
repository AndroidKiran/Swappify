package swapp.items.com.swappify.controller.addgame.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class GameModel @JvmOverloads constructor(var id: String? = null,
                                 var userNumber: String? = null,
                                 var name: String? = null,
                                 var url: String? = null,
                                 var platform: String? = null,
                                 var developer: String? = null,
                                 var genre: String? = null,
                                 var publisher: String? = null,
                                 var releaseDate: String? = null,
                                 var summary: String? = null,
                                 @ServerTimestamp
                                     var createdAt: Date? = null,
                                 @ServerTimestamp
                                     var modifiedAt: Date? = null) {

    companion object {
        const val CREATED_AT = "created_at"
    }
}
