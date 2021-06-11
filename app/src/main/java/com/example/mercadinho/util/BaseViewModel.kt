package com.example.mercadinho.util

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel<Intent, State>: ViewModel() {

    val state = SingleLiveEvent<State>()

    val disposable = CompositeDisposable()

    abstract fun handle(intent: Intent)

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

}