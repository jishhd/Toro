package toro.plus.josh.toro.models.objects

import android.os.Parcel
import android.os.Parcelable
import toro.plus.josh.toro.models.enums.Color

data class Message(
    val sender: String = "Sender",
    val receiver: String = "Receiver",
    val message: String = "Message",
    val dateReceived: String = "12/25/2018",
    val color: Color = Color.BLUE
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Message> = object : Parcelable.Creator<Message> {
            override fun createFromParcel(source: Parcel): Message = Message(source)
            override fun newArray(size: Int): Array<Message?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readSerializable() as Color
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(sender)
        dest.writeString(receiver)
        dest.writeString(message)
        dest.writeString(dateReceived)
        dest.writeSerializable(color)
    }

    override fun describeContents(): Int = 0

}
