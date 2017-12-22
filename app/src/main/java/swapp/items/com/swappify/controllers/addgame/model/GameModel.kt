package swapp.items.com.swappify.controllers.addgame.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import swapp.items.com.swappify.BR
import swapp.items.com.swappify.common.AppUtils


data class GameModel constructor(@get:Bindable
                                 @SerializedName("id")
                                 @Expose var id: Int?) : BaseObservable() {

    companion object {
        const val HTTPS = "https:"
    }

    @get:Bindable
    @SerializedName("name")
    @Expose
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }


    @get:Bindable
    @SerializedName("summary")
    @Expose
    var summary: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.summary)
        }

    @SerializedName("rating")
    @Expose
    var rating: Double? = null

    @SerializedName("genres")
    @Expose
    var genres: List<Int>? = null

    @SerializedName("first_release_date")
    @Expose
    var firstReleaseDate: Long? = null
        set(value) {
            if (value != null) {
                releaseDate = AppUtils.toMMMddyyyy(value)
            }
        }

    @get:Bindable
    @SerializedName("cover")
    @Expose
    var cover: Cover? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.cover)
        }

    @SerializedName("developers")
    @Expose
    var developers: List<Int>? = null

    @SerializedName("publishers")
    @Expose
    var publishers: List<Int>? = null

    @get:Bindable
    var releaseDate: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.releaseDate)
        }

    @get:Bindable
    var platform: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.platform)
        }

    @get:Bindable
    var developer: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.developer)
        }

    @get:Bindable
    var genre: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.genre)
        }

    @get:Bindable
    var publisher: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.publisher)
        }


    inner class Cover : BaseObservable() {

        @get:Bindable
        @SerializedName("url")
        @Expose
        var url: String? = null
            set(value) {
                field = if (!value?.startsWith(HTTPS)!!) {
                    HTTPS.plus(value)
                } else {
                    value
                }
                notifyPropertyChanged(BR.url)
            }

        @SerializedName("cloudinary_id")
        @Expose
        var cloudinaryId: String? = null
    }

    fun update(url: String?, releaseDate: Long?) {
        this.cover?.url = url
        this.firstReleaseDate = releaseDate
        notifyChange()
    }

}
