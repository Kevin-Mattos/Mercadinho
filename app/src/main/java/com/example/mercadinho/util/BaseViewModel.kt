package com.example.mercadinho.util

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<Intent, State>: ViewModel() {

    abstract val initialState: State
    protected val _state by lazy { MutableStateFlow(initialState) }
    val state = _state.asStateFlow()
    val disposable = CompositeDisposable()

    abstract fun handle(intent: Intent)

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}