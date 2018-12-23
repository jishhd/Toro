package toro.plus.josh.toro.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_message.*
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.Storage

class MessageActivity : AppCompatActivity() {
    lateinit var message: Message
    var color: Color = Storage.get(Data.LAST_USED_COLOR) as Color? ?: Color.RED
        set(color) {
            constraint_layout?.background = getDrawable(color.colorLight)
            message_date?.setTextColor(getColor(color.color))

            message_recipient?.setTextColor(getColor(color.colorDark))
            text_to?.background = getDrawable(color.colorLight)
            text_to?.boxStrokeColor = getColor(color.colorDark)

            message_body?.setTextColor(getColor(color.colorDark))
            text_body?.background = getDrawable(color.colorLight)
            text_body?.boxStrokeColor = getColor(color.colorDark)

            message_sender?.setTextColor(getColor(color.colorDark))
            text_from?.background = getDrawable(color.colorLight)
            text_from?.boxStrokeColor = getColor(color.colorDark)

            ViewCompat.setBackgroundTintList(bottom_bar, ColorStateList.valueOf(getColor(color.color)))
            ViewCompat.setBackgroundTintList(fab, ColorStateList.valueOf(getColor(color.colorAccent)))

            Storage.put(Data.LAST_USED_COLOR.key, color)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        message = (intent?.getParcelableExtra(Toro.EXTRA_MESSAGE) as Message?) ?: Message()

        message_date?.setText(message.dateReceived)
        message_recipient?.setText(message.recipient)
        message_body?.setText(message.message)
        message_sender?.setText(message.sender)

        fab?.setOnClickListener { save() }

        color = message.color
    }

    fun selectColor(v: View?) {
        when (v?.id) {
            R.id.btn_color_red -> color = Color.RED
            R.id.btn_color_orange -> color = Color.ORANGE
            R.id.btn_color_yellow -> color = Color.YELLOW
            R.id.btn_color_green -> color = Color.GREEN
            R.id.btn_color_blue -> color = Color.BLUE
            R.id.btn_color_purple -> color = Color.PURPLE
        }
    }

    fun save() {
        val message = Message(
            sender = message_sender?.text.toString(),
            recipient = message_recipient?.text.toString(),
            message = message_body?.text.toString(),
            dateReceived = Toro.getDate(),
            color = color
        )

        Storage.put(Data.NAME.key, message.sender)
        Storage.put(Data.MESSAGES.key, message)
        Storage.put(Data.LAST_USED_COLOR.key, message.color)

        setResult(RESULT_OK)
        finish()
    }
}
