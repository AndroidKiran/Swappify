package swapp.items.com.swappify.controller.addgame.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class GameModel constructor(var id: Int?,
                                 var userNumber: String?,
                                 var name: String?,
                                 var url: String?,
                                 var platform: String?,
                                 var developer: String?,
                                 var genre: String?,
                                 var publisher: String?,
                                 var releaseDate: String?,
                                 var summary: String?,
                                 @ServerTimestamp
                                     var createdAt: Date? = null,
                                 @ServerTimestamp
                                     var modifiedAt: Date? = null)
