package swapp.items.com.swappify.controller.addgame.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.common.Constant
import swapp.items.com.swappify.common.PreferenceHelper

@PaperParcel
data class SearchGameModel constructor(@SerializedName("id")
                                       var id: Int?,
                                       @SerializedName("rating")
                                       var rating: Double?,
                                       @SerializedName("genres")
                                       var genres: ArrayList<Int>?,
                                       @SerializedName("developers")
                                       var developers: ArrayList<Int>?,
                                       @SerializedName("publishers")
                                       var publishers: ArrayList<Int>?,
                                       @Bindable
                                       @SerializedName("cover")
                                       var cover: Cover?,
                                       @get:Bindable
                                       var url: String?,
                                       @get:Bindable
                                       @SerializedName("name")
                                       var name: String?,
                                       @get:Bindable
                                       @SerializedName("summary")
                                       var summary: String?,
                                       @SerializedName("first_release_date")
                                       var firstReleaseDate: Long?,
                                       @SerializedName("gameId")
                                       var gameId: String?,
                                       @get:Bindable
                                       var releaseDate: String?,
                                       @get:Bindable
                                       var platform: String?,
                                       @get:Bindable
                                       var developer: String?,
                                       @get:Bindable
                                       var genre: String?,
                                       @get:Bindable
                                       var publisher: String?) : BaseObservable(), PaperParcelable {


    constructor() : this(null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null)

    constructor(id: Int?, gameName: String?, gameUrl: String?, gamePlatform: String?,
                gameDeveloper: String?, gameGenre: String?, gamePublisher: String?,
                gameReleaseDate: String?, gameSummary: String?) : this(id, null, null, null, null,
            null, gameUrl, gameName, gameSummary, null, null, gameReleaseDate, gamePlatform, gameDeveloper,
            gameGenre, gamePublisher)


    fun update(url: String?, releaseDate: Long?) {
        setGameUrl(url)
        if (releaseDate != null) {
            setGameReleaseDate(AppUtils.toMMMddyyyy(releaseDate))
        }
    }

    fun setGameUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            this.url = if (!url?.startsWith(Constant.HTTPS)!!) {
                Constant.HTTPS.plus(url)
            } else {
                url
            }
            notifyPropertyChanged(BR.url)
        }
    }

    fun setGameUri(url: String?) {
        if (!url.isNullOrEmpty()) {
            this.url = url
            notifyPropertyChanged(BR.url)
        }
    }

    fun setGameName(name: String?) {
        if (!name.isNullOrEmpty()) {
            this.name = name
            notifyPropertyChanged(BR.name)
        }
    }

    fun setGamePlatform(platform: String?) {
        if (!platform.isNullOrEmpty()) {
            this.platform = platform
            notifyPropertyChanged(BR.platform)
        }
    }

    fun setGameDeveloper(developer: String?) {
        if (!developer.isNullOrEmpty()) {
            this.developer = developer
            notifyPropertyChanged(BR.developer)
        }
    }

    fun setGameGenre(genre: String?) {
        if (!genre.isNullOrEmpty()) {
            this.genre = genre
            notifyPropertyChanged(BR.genre)
        }
    }


    fun setGamePlublisher(publisher: String?) {
        if (!publisher.isNullOrEmpty()) {
            this.publisher = publisher
            notifyPropertyChanged(BR.publisher)
        }
    }

    fun setGameReleaseDate(releaseDate: String?) {
        if (!releaseDate.isNullOrEmpty()) {
            this.releaseDate = releaseDate
            notifyPropertyChanged(BR.releaseDate)
        }
    }

    fun setGameSummary(summary: String?) {
        if (!summary.isNullOrEmpty()) {
            this.summary = summary
            notifyPropertyChanged(BR.summary)
        }
    }

    fun toGameModel(preferenceHelper: PreferenceHelper): GameModel{
        val userId = AppUtils.getUserId(preferenceHelper)
        return GameModel(this.id, userId, this.name, this.url, this.platform, this.developer,
                this.genre, this.publisher, this.releaseDate, this.summary)
    }

    companion object {
        @JvmField
        val CREATOR = PaperParcelSearchGameModel.CREATOR
    }
}
