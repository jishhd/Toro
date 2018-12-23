package toro.plus.josh.toro.tools

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import toro.plus.josh.toro.models.enums.Keys

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
        }


        // UTILITIES

        @JvmStatic
        fun has(key: String): Boolean = db.contains(key)

        @JvmStatic
        fun put(key: String, value: String) = editor.putString(key, value).apply()

        @JvmStatic
        fun get(key: String, defValue: String? = null): String = db.getString(key, defValue) ?: ""

        @JvmStatic
        fun get(key: Keys): Any? = gson.fromJson(get(key.key, ""), key.token)

        @JvmStatic
        fun delete(key: String) = editor.remove(key).apply()

        @JvmStatic
        fun toJson(any: Any): String = gson.toJson(any)

        @JvmStatic
        fun format() = editor.clear().apply()
    }
}