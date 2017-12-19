package swapp.items.com.swappify.controllers.addgame.model

import com.google.gson.annotations.SerializedName

class GameModel {

    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

   /* @SerializedName("summary")
    var summary: String = ""

    @SerializedName("rating")
    var rating: Long = 0

    @SerializedName("genres")
    var genres = listOf<Int>()

    @SerializedName("first_release_date")
    var releaseDate: Long = 0

    @SerializedName("cover")
    var cover = Cover()

    @SerializedName("publishers")
    var publishers = listOf<Int>()

    @SerializedName("developers")
    var developers = listOf<Int>()

    inner class Cover {
        @SerializedName("url")
        var url: String = ""

        @SerializedName("cloudinary_id")
        var cloudinaryId: String = ""
    }*/
}
