package toro.plus.josh.toro.tools

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import com.google.gson.Gson
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.objects.Message

class Nfc(
    private val activity: Callback
) : NfcAdapter.CreateNdefMessageCallback,
    NfcAdapter.OnNdefPushCompleteCallback
{
    override fun createNdefMessage(event: NfcEvent): NdefMessage {
        // creating outcoming NFC message with a helper method
        // you could as well create it manually and will surely need, if Android version is too low
        val outString = Gson().toJson(activity.outgoingMessage)
        val outBytes = outString.toByteArray()
        val outRecord = NdefRecord.createMime(Toro.MIME_APPLICATION_JSON, outBytes)

        return NdefMessage(outRecord)
    }

    override fun onNdefPushComplete(event: NfcEvent) {
        // onNdefPushComplete() is called on the Binder thread, so remember to explicitly notify
        // your view on the UI thread
        activity.signalResult()
    }

    /*
    * Callback to be implemented by a Sender activity
    * */
    interface Callback {
        val outgoingMessage: Message
        fun signalResult()
    }
}