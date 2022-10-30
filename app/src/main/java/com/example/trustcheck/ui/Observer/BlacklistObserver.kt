package com.example.trustcheck.ui.Observer

import java.lang.ref.WeakReference
import java.util.*

object BlacklistObserver {
    private val observers: MutableList<WeakReference<Observer?>> = LinkedList()

    @JvmStatic
    fun addObserver(observer: Observer, immediate: Boolean) {
        observers.add(WeakReference(observer))
        if (immediate) observer.onBlacklistUpdate()
    }

    @JvmStatic
    fun removeObserver(observer: Observer) {
        for (ref in observers) if (ref.get() === observer) observers.remove(WeakReference(observer))
    }

    @JvmStatic
    fun notifyUpdated() {
        for (ref in observers) if (ref.get() != null) ref.get()!!
            .onBlacklistUpdate() else observers.remove(ref)
    }

    interface Observer {
        fun onBlacklistUpdate()
    }
}