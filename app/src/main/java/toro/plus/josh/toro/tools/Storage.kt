package toro.plus.josh.toro.tools

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import toro.plus.josh.toro.models.enums.Color
import toro.plus.josh.toro.models.enums.Data
import toro.plus.josh.toro.models.objects.Message

class Storage {
    companion object {
        const val SHARED_PREFERENCES = "toro.plus.josh.prefs"

        private lateinit var db: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private val gson = Gson()

        @JvmStatic
        fun init(context: Context) {
            db = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            editor = db.edit()

            setup(context)
        }

        private fun setup(context: Context) {
            if (!has(Data.NAME)) {
                //TODO if needed
            }

            if (!has(Data.MESSAGES)) {
                put(Data.MESSAGES, Message.getTutorialMessages(context))
            }

            if (!has(Data.LAST_USED_COLOR)) {
                put(Data.LAST_USED_COLOR, Color.BLUE)
            }
        }


        // UTILITIES

        @JvmStatic
        fun has(data: Data): Boolean = db.contains(data.key)

        @JvmStatic
        fun put(data: Data, value: Any) {
            editor.putString(data.key, toJson(value)).apply()
        }

        @JvmStatic
        fun add(data: Data, value: Any) {
            when (data.key) {
                Data.MESSAGES.key -> {
                    val messages = gson.fromJson(get(data, ""), Data.MESSAGES.token) as ArrayList<Message>? ?: arrayListOf()
                    messages.add(0, value as Message)
                    editor.putString(data.key, toJson(messages)).apply()
                }

                else -> editor.putString(data.key, toJson(value)).apply()
            }
        }

        @JvmStatic
        fun get(data: Data, defValue: String? = null): String = db.getString(data.key, defValue) ?: ""

        @JvmStatic
        fun get(data: Data): Any? = gson.fromJson(get(data, ""), data.token)

        @JvmStatic
        fun delete(key: String) = editor.remove(key).apply()

        @JvmStatic
        fun remove(data: Data, position: Int) {
            when (data.key) {
                Data.MESSAGES.key -> {
                    val messages = gson.fromJson(get(data, ""), Data.MESSAGES.token) as ArrayList<Message>? ?: arrayListOf()
                    if (messages.size > position) {
                        messages.removeAt(position)
                    }
                    editor.putString(data.key, toJson(messages)).apply()
                }

                else -> editor.remove(data.key).apply()
            }
        }

        @JvmStatic
        fun toJson(any: Any): String = gson.toJson(any)

        @JvmStatic
        fun format() = editor.clear().apply()
    }
}