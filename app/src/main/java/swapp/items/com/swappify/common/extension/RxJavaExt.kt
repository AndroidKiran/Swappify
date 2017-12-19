package swapp.items.com.swappify.common.extension

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.reactivestreams.Publisher
import swapp.items.com.swappify.rx.BaseSchedulerProvider

fun <T> Single<T>.getSingleAsync(schedulerProvider: BaseSchedulerProvider): Single<T>
        = this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Observable<T>.getObservableAsync(schedulerProvider: BaseSchedulerProvider): Observable<T>
        = this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Flowable<T>.getObservableAsync(schedulerProvider: BaseSchedulerProvider): Flowable<T>
        = this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)

fun <T> LiveData<T>.toPublisher(lifecycleOwner: LifecycleOwner)
        = LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)