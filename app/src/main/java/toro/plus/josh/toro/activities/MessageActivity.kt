package toro.plus.josh.toro.activities

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_message.*
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.NfcSender
import toro.plus.josh.toro.tools.Storage
import toro.plus.josh.toro.tools.UI

class MessageActivity : AppCompatActivity(), NfcSender.NfcActivity {
    private lateinit var message: Message

    private var nfcAdapter: NfcAdapter? = null
    private var outcomingNfccallback: NfcSender? = null

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private val isNfcSupported: Boolean
        get() {
            this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            return this.nfcAdapter != null
        }

    override val outcomingMessage: Message
        get() = Message(
            message_recipient.text.toString(),
            message_body.text.toString(),
            message_sender.text.toString(),
            message_date.text.toString(),
            color,
            false
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        message = (intent?.getParcelableExtra(Toro.EXTRA_MESSAGE) as Message?) ?: Message()

        message_date?.setText(message.dateReceived)
        message_recipient?.setText(message.recipient)
        message_body?.setText(message.message)
        message_sender?.setText(message.sender)

        fab?.setOnClickListener { save() }

        editable(message.editable)
        color = message.color

        if (!isNfcSupported) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show()
            finish()
        }
        if (!nfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show()
        }

        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = NfcSender(this)
        this.nfcAdapter?.setOnNdefPushCompleteCallback(outcomingNfccallback, this)
        this.nfcAdapter?.setNdefPushMessageCallback(outcomingNfccallback, this)
    }

    var color: Color = Storage.get(Data.LAST_USED_COLOR) as Color
        set(color) {
            updateUiColors(color)
            field = color
            if (::message.isInitialized) {
                if (message.editable) {
                    Storage.put(Data.LAST_USED_COLOR, color)
                }
            }
        }

    fun selectColor(v: View?) {
        when (v?.id) {
            R.id.btn_color_red    -> color = Color.RED
            R.id.btn_color_orange -> color = Color.ORANGE
            R.id.btn_color_yellow -> color = Color.YELLOW
            R.id.btn_color_green  -> color = Color.GREEN
            R.id.btn_color_blue   -> color = Color.BLUE
            R.id.btn_color_purple -> color = Color.PURPLE
        }
    }

    private fun updateUiColors(newColor: Color) {
        // layout background and date
        UI.updateBackgroundColor(this@MessageActivity, constraint_layout, color.colorLight, newColor.colorLight)
        UI.updateTextColor(this@MessageActivity, message_date, color.colorDark, newColor.colorDark)
        // recipient fields
        UI.updateTextColor(this@MessageActivity, message_recipient, color.colorDark, newColor.colorDark)
        UI.updateHighlightColor(this@MessageActivity, message_recipient, color.color, newColor.color)
        UI.updateCardBackgroundColor(this@MessageActivity, card_recipient, color.colorPale, newColor.colorPale)
        UI.updateBackgroundColor(this@MessageActivity, text_to, color.colorPale, newColor.colorPale)
        UI.updateBoxStrokeColor(this@MessageActivity, text_to, color.colorDark, newColor.colorDark)
        // message body
        UI.updateTextColor(this@MessageActivity, message_body, color.colorDark, newColor.colorDark)
        UI.updateHighlightColor(this@MessageActivity, message_body, color.color, newColor.color)
        UI.updateCardBackgroundColor(this@MessageActivity, card_body, color.colorPale, newColor.colorPale)
        UI.updateBackgroundColor(this@MessageActivity, text_body, color.colorPale, newColor.colorPale)
        UI.updateBoxStrokeColor(this@MessageActivity, text_body, color.colorDark, newColor.colorDark)
        // sender fields
        UI.updateTextColor(this@MessageActivity, message_sender, color.colorDark, newColor.colorDark)
        UI.updateHighlightColor(this@MessageActivity, message_sender, color.color, newColor.color)
        UI.updateCardBackgroundColor(this@MessageActivity, card_sender, color.colorPale, newColor.colorPale)
        UI.updateBackgroundColor(this@MessageActivity, text_from, color.colorPale, newColor.colorPale)
        UI.updateBoxStrokeColor(this@MessageActivity, text_from, color.colorDark, newColor.colorDark)
        // bottom bar and fab
        UI.updateTintListColor(this@MessageActivity, bottom_bar, color.color, newColor.color)
        UI.updateTintListColor(this@MessageActivity, fab, color.colorAccent, newColor.colorAccent)
    }

    fun editable(editable: Boolean) {
        // hide bottom bar
        if (editable) {
            fab?.show()
            bottom_bar?.visibility = View.VISIBLE
        } else {
            fab?.hide()
            bottom_bar?.visibility = View.GONE
        }
        // text fields
        message_recipient?.isEnabled = editable
        message_body?.isEnabled = editable
        message_sender?.isEnabled = editable
        // buttons
        btn_color_red?.isEnabled = editable
        btn_color_orange?.isEnabled = editable
        btn_color_yellow?.isEnabled = editable
        btn_color_green?.isEnabled = editable
        btn_color_blue?.isEnabled = editable
        btn_color_purple?.isEnabled = editable
        fab?.isEnabled = editable
    }

    fun save() {
        val message = Message(
            sender = message_sender?.text.toString(),
            recipient = message_recipient?.text.toString(),
            message = message_body?.text.toString(),
            dateReceived = Toro.getDate(),
            color = color,
            editable = true
        )

        Storage.put(Data.NAME, message.sender)
        Storage.add(Data.SENT_MESSAGES, message)
        Storage.put(Data.LAST_USED_COLOR, message.color)

        setResult(RESULT_OK)
        finish()
    }

    fun setMessage(message: Message) {
        updateFields(message)
        updateUiColors(message.color)
        Storage.add(Data.RECEIVED_MESSAGES, message)
    }

    fun getCurrentMessage(): Message = Message(
        message_recipient.text.toString(),
        message_body.text.toString(),
        message_sender.text.toString(),
        message_date.text.toString(),
        color
    )

    fun updateFields(message: Message) {
        this.message = Message(
            message.recipient,
            message.message,
            message.sender,
            Toro.getDate(),
            message.color,
            false
        )

        message_date?.setText(this.message.dateReceived)
        message_recipient?.setText(this.message.recipient)
        message_body?.setText(this.message.message)
        message_sender?.setText(this.message.sender)
        editable(message.editable)
    }


    // NFC

    override fun onNewIntent(intent: Intent) {
        receiveMessageFromDevice(intent)
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
        receiveMessageFromDevice(intent)
    }

    override fun onPause() {
        super.onPause()
        disableForegroundDispatch(this, this.nfcAdapter)
    }

    private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            val inNdefMessage = parcelables[0] as NdefMessage
            val inNdefRecords = inNdefMessage.records
            val ndefRecord_0 = inNdefRecords[0]

            val inMessage = String(ndefRecord_0.payload)
            //TODO: set text with inMessage
            try {
                val message = Message.fromJson(inMessage)
                if (message != null) {
                    setMessage(message)
                } else {
                    UI.pop(this@MessageActivity, "Error Parsing Message")
                }
            } catch (e: Exception) {
                UI.pop(this@MessageActivity, "Error Parsing Message")
            }
        }
    }


    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        //
        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        filters[0]?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]?.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]?.addDataType(Toro.MIME_APPLICATION_JSON)
        } catch (ex: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("Check your MIME type")
        }

        adapter!!.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter!!.disableForegroundDispatch(activity)
    }

    override fun signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        Storage.add(Data.SENT_MESSAGES, getCurrentMessage())
        runOnUiThread { UI.pop(this@MessageActivity, "Beaming Complete!") }
    }
}
