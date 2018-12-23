package toro.plus.josh.toro

import android.app.Application
import toro.plus.josh.toro.tools.Storage

class Toro() : Application() {

    companion object {
        val TAG = Toro::class.java.name
        const val STRING = "string"
        const val MESSAGES = "messages"

        // request codes
        const val REQUEST_MESSAGE = 0

        // extras
        @JvmStatic
        val EXTRA_MESSAGE = "$TAG.message"
    }

    override fun onCreate() {
        super.onCreate()
        Storage.init(this)
    }
}