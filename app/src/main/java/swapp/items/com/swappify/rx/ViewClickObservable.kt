package swapp.items.com.swappify.rx

import android.view.View
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import swapp.items.com.swappify.rx.Preconditions.Companion.checkMainThread


internal class ViewClickObservable(private val view: View) : Observable<Any>() {

    override fun subscribeActual(observer: Observer<in Any>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(view, observer)
        observer.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    internal class Listener(private val view: View, private val observer: Observer<in Any>) : MainThreadDisposable(), View.OnClickListener {

        override fun onClick(v: View) {
            if (!isDisposed) {
                observer.onNext(Notification.INSTANCE)
            }
        }

        override fun onDispose() {
            view.setOnClickListener(null)
        }
    }
}
