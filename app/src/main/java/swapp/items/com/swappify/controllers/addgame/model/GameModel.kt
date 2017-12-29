package swapp.items.com.swappify.controllers.addgame.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.common.AppUtils
import swapp.items.com.swappify.common.Constant

@PaperParcel
data class GameModel constructor(@SerializedName("id")
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
            null, null, null, null)

//    @get:Bindable
//    @SerializedName("url")
//    var url: String? = null
//        set(value) {
//            if (value != null) {
//                field = if (!value.startsWith(Constant.HTTPS)) {
//                    Constant.HTTPS.plus(value)
//                } else {
//                    value
//                }
//                notifyPropertyChanged(BR.url)
//            }
//        }

//    @get:Bindable
//    @SerializedName("name")
//    var name: String? = null
//        set(value) {
//            if (value != null) {
//                field = value
//                notifyPropertyChanged(BR.name)
//            }
//        }

//    @get:Bindable
//    @SerializedName("summary")
//    var summary: String? = null
//        set(value) {
//            if (value != null) {
//                field = value
//                notifyPropertyChanged(BR.summary)
//            }
//        }

    /*@SerializedName("first_release_date")
    var firstReleaseDate: Long? = null
        set(value) {
            if (value != null) {
                field = value
                releaseDate = AppUtils.toMMMddyyyy(value)
            }
        }*/

    /*  @get:Bindable
      var releaseDate: String? = null
          set(value) {
              if (value != null) {
                  field = value
                  notifyPropertyChanged(BR.releaseDate)
              }
          }*/

    /*@get:Bindable
    var platform: String? = null
        set(value) {
            if (value != null) {
                field = value
                notifyPropertyChanged(BR.platform)
            }
        }*/

    /*@get:Bindable
    var developer: String? = null
        set(value) {
            if (value != null) {
                field = value
                notifyPropertyChanged(BR.developer)
            }
        }*/

    /*@get:Bindable
    var genre: String? = null
        set(value) {
            if (value != null) {
                field = value
                notifyPropertyChanged(BR.genre)
            }
        }*/

    /* @get:Bindable
     var publisher: String? = null
         set(value) {
             if (value != null) {
                 field = value
                 notifyPropertyChanged(BR.publisher)
             }
         }*/

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

    companion object {
        @JvmField
        val CREATOR = PaperParcelGameModel.CREATOR
    }
}
