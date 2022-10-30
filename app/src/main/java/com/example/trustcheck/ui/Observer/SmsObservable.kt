package com.example.trustcheck.ui.Observer

import java.util.*

class SmsObservable private constructor() : Observable() {
    fun update(marker: NewMessageMarker?) {
        synchronized(this) {
            setChanged()
            this.notifyObservers(marker)
        }
    }

    /**
     * When a new SMS messages is received, a marker object will be created prior to the
     * message being saved in the SMS inbox. The marker can then be used to query for the
     * new message.
     */
    class NewMessageMarker(var address: String) {
        var ts = System.currentTimeMillis()
    }

    companion object {
        val instance = SmsObservable()
    }
}