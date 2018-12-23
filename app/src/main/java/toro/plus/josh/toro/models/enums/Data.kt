package toro.plus.josh.toro.models.enums

import com.google.gson.reflect.TypeToken
import toro.plus.josh.toro.models.objects.Message
import toro.plus.josh.toro.tools.Storage
import java.lang.reflect.Type

enum class Data(val key: String, val token: Type) {
    NAME(
        "${Storage.SHARED_PREFERENCES}.name",
        object : TypeToken<String>() {}.type
    ),
    MESSAGES(
        "${Storage.SHARED_PREFERENCES}.messages",
        object : TypeToken<ArrayList<Message>>() {}.type
    ),
    LAST_USED_COLOR(
        "${Storage.SHARED_PREFERENCES}.last_used_color",
        object : TypeToken<Color>() {}.type
    )
}