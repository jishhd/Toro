package toro.plus.josh.toro.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_message.*
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.Storage
import toro.plus.josh.toro.tools.UI


class MessageActivity : AppCompatActivity() {
    lateinit var message: Message

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
        UI.updateTextColor(this@MessageActivity, message_date, color.color, newColor.color)
        // recipient fields
        UI.updateTextColor(this@MessageActivity, message_recipient, color.colorDark, newColor.colorDark)
        UI.updateHighlightColor(this@MessageActivity, message_recipient, color.color, newColor.color)
        UI.updateBackgroundColor(this@MessageActivity, text_to, color.colorPale, newColor.colorPale)
        UI.updateBoxStrokeColor(this@MessageActivity, text_to, color.colorDark, newColor.colorDark)
        // message body
        UI.updateTextColor(this@MessageActivity, message_body, color.colorDark, newColor.colorDark)
        UI.updateHighlightColor(this@MessageActivity, message_body, color.color, newColor.color)
        UI.updateBackgroundColor(this@MessageActivity, text_body, color.colorPale, newColor.colorPale)
        UI.updateBoxStrokeColor(this@MessageActivity, text_body, color.colorDark, newColor.colorDark)
        // sender fields
        UI.updateTextColor(this@MessageActivity, message_sender, color.colorDark, newColor.colorDark)
        UI.updateHighlightColor(this@MessageActivity, message_sender, color.color, newColor.color)
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
            color = color
        )

        Storage.put(Data.NAME, message.sender)
        Storage.add(Data.SENT_MESSAGES, message)
        Storage.put(Data.LAST_USED_COLOR, message.color)

        setResult(RESULT_OK)
        finish()
    }
}
