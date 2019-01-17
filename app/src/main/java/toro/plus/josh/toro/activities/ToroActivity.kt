package toro.plus.josh.toro.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import kotlinx.android.synthetic.main.activity_toro.*
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.models.enums.Filter
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.Storage
import toro.plus.josh.toro.tools.UI

class ToroActivity : AppCompatActivity() {

    val messagesSent = arrayListOf<Message>()
    val messagesReceived = arrayListOf<Message>()
    val adapter = MessageAdapter()
    var filter = Filter.RECEIVED
        set(filter) {
            field = filter
            updateFilter(filter)
        }
    var color: Color = Storage.get(Data.LAST_USED_COLOR) as Color
        set(color) {
            updateUiColors(color)
            field = color
            Storage.put(Data.LAST_USED_COLOR, color)
        }
    private lateinit var installReferrer: InstallReferrerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toro)

        message_recycler?.adapter = adapter
        message_recycler?.itemAnimator = DefaultItemAnimator()
        btn_filter?.setOnClickListener { rotateFilter() }
        fab?.setOnClickListener { goToMessage(Message()) }

        messagesSent.addAll((Storage.get<ArrayList<Message>>(Data.SENT_MESSAGES)))
        messagesReceived.addAll((Storage.get<ArrayList<Message>>(Data.RECEIVED_MESSAGES)))
        UI.animateLoad(message_recycler)

        if (!(Storage.has(Data.LAUNCHED))) {
            initInstallReferrer()
        }
        Storage.put(Data.LAUNCHED, true)
    }


    // UTLITIES

    fun initInstallReferrer() {
        installReferrer = InstallReferrerClient.newBuilder(this).build()
        installReferrer.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established
                        val response: ReferrerDetails = installReferrer.installReferrer

                        UI.pop(response.installReferrer)

                        installReferrer.endConnection()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection could not be established
                    }
                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
                        // A developer error?
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {
                        // Disconnected from service
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    fun goToMessage(message: Message) {
        val intent = Intent(this@ToroActivity, MessageActivity::class.java)
        intent.putExtra(Toro.EXTRA_OUTGOING_MESSAGE, message)
        startActivityForResult(intent, Toro.REQUEST_MESSAGE)
    }

    fun updatedMessages(filter: Filter): ArrayList<Message> = when (filter) {
        Filter.SENT -> (Storage.get(Data.SENT_MESSAGES) as ArrayList<Message>?) ?: arrayListOf()
        Filter.RECEIVED -> (Storage.get(Data.RECEIVED_MESSAGES) as ArrayList<Message>?) ?: arrayListOf()
    }

    fun deleteMessageAt(position: Int) {
        UI.showDeleteDialog(Runnable{
            when (filter) {
                Filter.SENT -> {
                    messagesSent.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Storage.put(Data.SENT_MESSAGES, messagesSent)
                    Toast.makeText(this@ToroActivity, "Deleted", Toast.LENGTH_SHORT).show()
                }

                Filter.RECEIVED -> {
                    messagesReceived.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Storage.put(Data.RECEIVED_MESSAGES, messagesReceived)
                    Toast.makeText(this@ToroActivity, "Deleted", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun rotateFilter() = when (filter) {
        Filter.SENT -> filter = Filter.RECEIVED
        Filter.RECEIVED -> filter = Filter.SENT
    }

    fun updateFilter(filter: Filter) {
        btn_filter?.text = filter.title
        when (filter) {
            Filter.SENT -> {
                adapter.notifyItemRangeRemoved(0, messagesReceived.size)
                adapter.notifyItemRangeInserted(0, messagesSent.size)
            }
            Filter.RECEIVED -> {
                adapter.notifyItemRangeRemoved(0, messagesSent.size)
                adapter.notifyItemRangeInserted(0, messagesReceived.size)
            }
        }

        UI.animateLoad(message_recycler)
    }

    private fun updateUiColors(newColor: Color) {
        UI.updateBackgroundColor(this@ToroActivity, toro_layout, color.colorPale, newColor.colorPale)
        // bottom bar and fab
        UI.updateTintListColor(this@ToroActivity, bottom_bar, color.color, newColor.color)
        UI.updateTintListColor(this@ToroActivity, fab, color.colorAccent, newColor.colorAccent)
        UI.updateTintListColor(this@ToroActivity, btn_filter, color.colorPale, newColor.colorPale)
    }


    // INNER CLASSES

    inner class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
            return MessageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.message_preview,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int = when (filter) {
            Filter.SENT -> messagesSent.size
            Filter.RECEIVED -> messagesReceived.size
        }

        override fun onBindViewHolder(holder: MessageHolder, position: Int) {
            val message = when (filter) {
                Filter.SENT -> messagesSent[position]
                Filter.RECEIVED -> messagesReceived[position]
            }

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
            holder.date.setTextColor(ContextCompat.getColor(this@ToroActivity, color.colorDark))
        }

        inner class MessageHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
            val card: CardView = view.findViewById(R.id.card)
            val to: AppCompatTextView = view.findViewById(R.id.to)
            val from: AppCompatTextView = view.findViewById(R.id.from)
            val message: AppCompatTextView = view.findViewById(R.id.message)
            val date: AppCompatTextView = view.findViewById(R.id.date)

            init {
                card.setOnClickListener(this)
                card.setOnLongClickListener(this)
            }

            override fun onClick(v: View?) = when (filter) {
                Filter.SENT -> goToMessage(messagesSent[adapterPosition])
                Filter.RECEIVED -> goToMessage(messagesReceived[adapterPosition])
            }

            override fun onLongClick(v: View?): Boolean {
                deleteMessageAt(adapterPosition)
                return true
            };
        }
    }


    // OVERRIDES

    override fun onPostResume() {
        super.onPostResume()
        color = Storage.get(Data.LAST_USED_COLOR) as Color
        messagesSent.clear()
        messagesSent.addAll(updatedMessages(Filter.SENT))
        messagesReceived.clear()
        messagesReceived.addAll(updatedMessages(Filter.RECEIVED))
        UI.animateLoad(message_recycler)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Toro.REQUEST_MESSAGE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        filter = Filter.SENT
                    }
                }
            }
        }
    }
}
