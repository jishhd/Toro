package toro.plus.josh.toro.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_edit_message.*
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.objects.Message

class EditMessageActivity : AppCompatActivity() {
    lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_message)

        message = (intent?.getParcelableExtra(Toro.EXTRA_MESSAGE) as Message?) ?: Message()

        edit_message?.text = message.message
        setColor(message.color)
    }

    private fun setColor(color: Color) {
        edit_message?.background = getDrawable(color.color)
        ViewCompat.setBackgroundTintList(bottom_bar, ColorStateList.valueOf(getColor(color.colorDark)))
        ViewCompat.setBackgroundTintList(fab, ColorStateList.valueOf(getColor(color.colorAccent)))
    }

    fun selectColor(v: View?) {
        when (v?.id) {
            R.id.btn_color_red -> setColor(Color.RED)
            R.id.btn_color_orange -> setColor(Color.ORANGE)
            R.id.btn_color_yellow -> setColor(Color.YELLOW)
            R.id.btn_color_green -> setColor(Color.GREEN)
            R.id.btn_color_blue -> setColor(Color.BLUE)
            R.id.btn_color_purple -> setColor(Color.PURPLE)
        }
    }
}
