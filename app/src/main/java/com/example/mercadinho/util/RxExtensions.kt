package com.example.mercadinho.util

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


fun <T> Single<T>.singleSubscribe(
    onLoading: ((Boolean) -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
    onSuccess: ((T) -> Unit)? = null,
    subscribeOnScheduler: Scheduler? = Schedulers.io(),
    observeOnScheduler: Scheduler? = AndroidSchedulers.mainThread()
) = subscribeOn(subscribeOnScheduler)
    .observeOn(observeOnScheduler)
    .doOnSubscribe { onLoading?.invoke(true) }
    .subscribeWith(object : DisposableSingleObserver<T>() {
        override fun onSuccess(value: T) {
            onLoading?.invoke(false)
            onSuccess?.invoke(value)
        }

        override fun onError(e: Throwable) {
            onLoading?.invoke(false)
            onError?.invoke(e)
        }

    })


fun Completable.completableSubscribe(
    onLoading: ((Boolean) -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    subscribeOnScheduler: Scheduler? = Schedulers.io(),
    observeOnScheduler: Scheduler? = AndroidSchedulers.mainThread()
) = subscribeOn(subscribeOnScheduler)
    .observeOn(observeOnScheduler)
    .doOnSubscribe { onLoading?.invoke(true) }
    .subscribeWith(object : DisposableCompletableObserver() {
        override fun onComplete() {
            onLoading?.invoke(false)
            onComplete?.invoke()
        }

        override fun onError(e: Throwable) {
            onLoading?.invoke(false)
            onError?.invoke(e)
        }


    })


fun<T> Flowable<T>.flowableSubscribe(
    onLoading: ((Boolean) -> Unit)? = null,
    onError: ((Throwable?) -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onNext: ((T) -> Unit)? = null,
    subscribeOnScheduler: Scheduler = Schedulers.io(),
    observeOnScheduler: Scheduler = AndroidSchedulers.mainThread()
) = subscribeOn(subscribeOnScheduler)
    .observeOn(observeOnScheduler)
    .doOnSubscribe { onLoading?.invoke(true) }
    .subscribeWith(object : DisposableSubscriber<T>() {
        override fun onComplete() {
            onLoading?.invoke(false)
            onComplete?.invoke()
        }

        override fun onError(e: Throwable) {
            onLoading?.invoke(false)
            onError?.invoke(e)
        }

        override fun onNext(t: T) {
            onLoading?.invoke(false)
            onNext?.invoke(t)
            Log.d("Flowable on Next", "$t")
        }


    })