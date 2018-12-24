package toro.plus.josh.toro.models.objects

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import toro.plus.josh.toro.BuildConfig
import toro.plus.josh.toro.R
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.tools.Storage


data class Message(
    var recipient: String = "",
    var message: String = "",
    var sender: String = Storage.get(Data.NAME) as String? ?: "",
    var dateReceived: String = Toro.getDate(),
    var color: Color = Storage.get(Data.LAST_USED_COLOR) as Color? ?: Color.RED,
    var editable: Boolean = true

) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Message> = object : Parcelable.Creator<Message> {
            override fun createFromParcel(source: Parcel): Message = Message(source)
            override fun newArray(size: Int): Array<Message?> = arrayOfNulls(size)
        }

        @JvmStatic
        fun getTutorialMessages(context: Context): ArrayList<Message> = arrayListOf(
            Message(
                recipient = context.getString(R.string.card_1_intro),
                message = context.getString(R.string.card_1_body),
                sender = context.getString(R.string.card_1_outro),
                dateReceived = BuildConfig.VERSION_NAME,
                color = Color.RED,
                editable = false
            ),
            Message(
                recipient = context.getString(R.string.card_1_intro),
                message = context.getString(R.string.card_1_body),
                sender = context.getString(R.string.card_1_outro),
                dateReceived = BuildConfig.VERSION_NAME,
                color = Color.ORANGE,
                editable = false
            ),
            Message(
                recipient = context.getString(R.string.card_1_intro),
                message = context.getString(R.string.card_1_body),
                sender = context.getString(R.string.card_1_outro),
                dateReceived = BuildConfig.VERSION_NAME,
                color = Color.YELLOW,
                editable = false
            ),
            Message(
                recipient = context.getString(R.string.card_1_intro),
                message = context.getString(R.string.card_1_body),
                sender = context.getString(R.string.card_1_outro),
                dateReceived = BuildConfig.VERSION_NAME,
                color = Color.GREEN,
                editable = false
            ),
            Message(
                recipient = context.getString(R.string.card_1_intro),
                message = context.getString(R.string.card_1_body),
                sender = context.getString(R.string.card_1_outro),
                dateReceived = BuildConfig.VERSION_NAME,
                color = Color.BLUE,
                editable = false
            ),
            Message(
                recipient = context.getString(R.string.card_1_intro),
                message = context.getString(R.string.card_1_body),
                sender = context.getString(R.string.card_1_outro),
                dateReceived = BuildConfig.VERSION_NAME,
                color = Color.PURPLE,
                editable = false
            )
        )
    }

    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readSerializable() as Color? ?: Storage.get(Data.LAST_USED_COLOR) as Color? ?: Color.RED,
        if (source.readByte().equals(1.toByte())) {true} else {false}
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(recipient)
        dest.writeString(message)
        dest.writeString(sender)
        dest.writeString(dateReceived)
        dest.writeSerializable(color)
        dest.writeByte(if (editable) {1} else {0})
    }

    override fun describeContents(): Int = 0
}
