package toro.plus.josh.toro

import android.app.Application
import toro.plus.josh.toro.tools.Storage
import java.text.SimpleDateFormat
import java.util.*

class Toro() : Application() {

    companion object {
        val TAG = Toro::class.java.name

        lateinit var instance: Toro
            private set

        // constants
        const val BLANK = ""
        const val DATE_STRING = "MM/dd/yyyy"
        const val MIME_APPLICATION_JSON = "application/json"

        // request codes
        const val REQUEST_MESSAGE = 0

        // extras
        @JvmStatic
        val EXTRA_OUTGOING_MESSAGE = "$TAG.outgoingMessage"
        @JvmStatic
        val EXTRA_INCOMING_MESSAGE = "$TAG.incomingMessage"

        // utilities
        @JvmStatic
        fun getDate() = SimpleDateFormat(DATE_STRING, Locale.US).format(Calendar.getInstance().time)
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        Storage.init()
    }
}