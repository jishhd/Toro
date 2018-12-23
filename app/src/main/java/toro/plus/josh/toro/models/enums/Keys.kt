package toro.plus.josh.toro.models.enums

import com.google.gson.reflect.TypeToken
import toro.plus.josh.toro.Toro
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.Storage
import java.lang.reflect.Type

enum class Keys(val key: String, val type: String, val token: Type) {
    NAME(
        "${Storage.SHARED_PREFERENCES}.name",
        Toro.STRING,
        object : TypeToken<String>() {}.type
    ),
    MESSAGES(
        "${Storage.SHARED_PREFERENCES}.messages",
        Toro.MESSAGES,
        object : TypeToken<Array<Message>>() {}.type
    )
}