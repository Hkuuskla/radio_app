package ee.itcollege.hkuusk.intentdemo2019f

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class PhoneEventReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = this::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive action " + intent?.action + " context " + context)

        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            LocalBroadcastManager.getInstance(context)
                .sendBroadcast(Intent(C.INTENT_PHONE_IS_RINGING))
            //Toast.makeText(context, "Phone is ringing", Toast.LENGTH_LONG).show();
        }
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            LocalBroadcastManager.getInstance(context)
                .sendBroadcast(Intent(C.INTENT_PHONE_CALL_RECEIVED))
            //Toast.makeText(context, "Call Recieved", Toast.LENGTH_LONG).show();
        }

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(C.INTENT_PHONE_IS_IDLE))
            //Toast.makeText(context, "Phone Is Idle", Toast.LENGTH_LONG).show();
        }

    }


}
