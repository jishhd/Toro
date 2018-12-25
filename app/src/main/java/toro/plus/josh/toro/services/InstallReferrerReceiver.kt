//package toro.plus.josh.toro.services
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.util.Log
//import com.google.gson.Gson
//import com.google.gson.JsonParseException
//import toro.plus.josh.toro.models.objects.Message
//import toro.plus.josh.toro.tools.UI
//
//class InstallReferrerReceiver : BroadcastReceiver() {
//    private val TAG = InstallReferrerReceiver::class.java.simpleName
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val user = intent.getStringExtra("referrer")
//        if (user != null) {
//            try {
//                val message = Message.fromJson(user)
//                if (message != null) {
//                    UI.pop(context, user)
//                } else {
//                    Log.e(TAG, "No message received")
//                }
//
//            } catch (e: JsonParseException) {
//                Log.e(TAG, e.toString())
//            }
//        }
//    }
//}