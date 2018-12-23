package toro.plus.josh.toro.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_toro.*
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.Storage

class ToroActivity : AppCompatActivity() {

    val messages = arrayListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toro)

        message_recycler?.adapter = MessageAdapter()
        messages.addAll((Storage.get(Data.MESSAGES) as ArrayList<Message>?) ?: arrayListOf(Message()))

        fab?.setOnClickListener {
            goToMessage(Message())
        }
    }


    // INNER CLASSES

    inner class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
            return MessageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.card_message_preview,
                    parent,
                    false
                )
            )
        }

        fun update() {
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int = messages.size

        override fun onBindViewHolder(holder: MessageHolder, position: Int) {
            val message = messages[position]

            holder.from.text = message.sender
            holder.to.text = message.recipient
            holder.date.text = message.dateReceived
            holder.message.text = message.message

            colorCard(holder, message.color)
        }

        private fun colorCard(holder: MessageHolder, color: Color) {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(this@ToroActivity, color.colorLight))
            holder.from.setTextColor(ContextCompat.getColor(this@ToroActivity, color.colorDark))
            holder.to.setTextColor(ContextCompat.getColor(this@ToroActivity, color.colorDark))
            holder.message.setTextColor(ContextCompat.getColor(this@ToroActivity, color.colorDark))
            holder.date.setTextColor(ContextCompat.getColor(this@ToroActivity, color.colorAccent))
        }

        inner class MessageHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
            val card: CardView = view.findViewById(R.id.card)
            val to: AppCompatTextView = view.findViewById(R.id.to)
            val from: AppCompatTextView = view.findViewById(R.id.from)
            val message: AppCompatTextView = view.findViewById(R.id.message)
            val date: AppCompatTextView = view.findViewById(R.id.date)

            init {
                card.setOnClickListener(this)
            }

            override fun onClick(v: View?) = goToMessage(messages[adapterPosition])
        }
    }


    // UTLITIES

    fun goToMessage(message: Message) {
        val intent = Intent(this@ToroActivity, MessageActivity::class.java)
        intent.putExtra(Toro.EXTRA_MESSAGE, message)
        startActivityForResult(intent, Toro.REQUEST_MESSAGE)
    }

    fun updatedMessages(): ArrayList<Message> = (Storage.get(Data.MESSAGES) as ArrayList<Message>?) ?: arrayListOf(Message())


    // OVERRIDES

    override fun onPostResume() {
        super.onPostResume()
        messages.clear()
        messages.addAll(updatedMessages())
        (message_recycler?.adapter as MessageAdapter?)?.update()
    }
}
