package swapp.items.com.swappify.rx.utils

import io.reactivex.Observable
import io.reactivex.Single
import swapp.items.com.swappify.rx.BaseSchedulerProvider

fun <T> Single<T>.getSingleAsync(schedulerProvider: BaseSchedulerProvider): Single<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Observable<T>.getObservableAsync(schedulerProvider: BaseSchedulerProvider): Observable<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())