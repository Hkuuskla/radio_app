package ee.itcollege.hkuusk.intentdemo2019f

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager



class PhoneEventReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = this::class.java.simpleName
    }



    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive action " + intent?.action + " context " + context)
        when (intent?.action) {
            C.INTENT_PHONE_STATE -> {
                MusicService.mediaPlayer.stop()
                LocalBroadcastManager
                    .getInstance(context)
                    .sendBroadcast(Intent(C.INTENT_PLAYER_STOPPED))
            }
        }
    }


}
