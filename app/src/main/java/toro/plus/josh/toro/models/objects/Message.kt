package toro.plus.josh.toro.models.objects

import android.os.Parcel
import android.os.Parcelable
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.tools.Storage


data class Message(
    val sender: String = "",
    val recipient: String = "",
    val message: String = "",
    val dateReceived: String = Toro.getDate(),
    val color: Color = Storage.get(Data.LAST_USED_COLOR) as Color? ?: Color.RED

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
        dest.writeString(recipient)
        dest.writeString(message)
        dest.writeString(dateReceived)
        dest.writeSerializable(color)
    }

    override fun describeContents(): Int = 0

}
