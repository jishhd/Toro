package toro.plus.josh.toro

import android.app.Application
import toro.plus.josh.toro.tools.Storage
import java.text.SimpleDateFormat
import java.util.*

class Toro() : Application() {

    companion object {
        val TAG = Toro::class.java.name

        // constants
        const val DATE_STRING = "MM/dd/yyyy"

        // request codes
        const val REQUEST_MESSAGE = 0

        // extras
        @JvmStatic
        val EXTRA_MESSAGE = "$TAG.message"

        // utilities
        @JvmStatic
        fun getDate() = SimpleDateFormat(DATE_STRING, Locale.US).format(Calendar.getInstance().time)
    }

    override fun onCreate() {
        super.onCreate()
        Storage.init(this)
    }
}