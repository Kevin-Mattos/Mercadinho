package com.merc.mercadao.util

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<State> : MutableLiveData<State>() {

    private val pending = AtomicBoolean(false)

    override fun setValue(value: State?) {
        pending.set(true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in State>) {

        if (hasActiveObservers())
            Log.w("", "only first observer will be notified")

        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false))
                observer.onChanged(t)
        }
    }

}