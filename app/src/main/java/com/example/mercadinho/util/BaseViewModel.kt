package com.example.mercadinho.util

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Intent, State>: ViewModel() {

    val state = SingleLiveEvent<State>()

    abstract fun handle(intent: Intent)

}