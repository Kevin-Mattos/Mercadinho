package com.example.mercadinho.util

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber


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