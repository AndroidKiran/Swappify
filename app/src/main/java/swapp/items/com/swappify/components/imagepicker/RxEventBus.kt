package swapp.items.com.swappify.components.imagepicker

import android.net.Uri
import io.reactivex.subjects.PublishSubject


object RxEventBus {

    var publishSubject: PublishSubject<Uri>? = null

    fun getObservable(): PublishSubject<Uri>? {
        if (publishSubject == null) {
            publishSubject = PublishSubject.create()
        }
        return publishSubject
    }
}
