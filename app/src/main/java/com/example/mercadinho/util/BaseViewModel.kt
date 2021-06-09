package com.example.mercadinho.util

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel<Intent, State>: ViewModel() {

    val state = SingleState<State>()

    abstract fun handle(intent: Intent)

}

abstract class BaseViewModelFactory(private val factory: () -> ViewModel): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factory () as T
    }

}

class SingleState<State>: MutableLiveData<State>() {


    override fun setValue(value: State?) {
        //CoroutineScope(Dispatchers.Main).let {
            super.setValue(value)
      //  }
    }

}