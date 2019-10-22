package ee.itcollege.hkuusk.intentdemo2019f

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val localReceiver = BroadcastReceiverInMainActivity()
    private val localReceiverIntentFilter: IntentFilter = IntentFilter()

    private var playerStatus = C.PLAYER_STATUS_STOPPED


    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    //private var editTextName : EditText? = null
    //private var textViewGreeting : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //editTextName = findViewById(R.id.editTextName)
        //textViewGreeting = findViewById(R.id.textViewGreeting)
        localReceiverIntentFilter.addAction(C.INTENT_SERVICE_TIME)
        localReceiverIntentFilter.addAction(C.INTENT_PLAYER_STOPPED)
        localReceiverIntentFilter.addAction(C.INTENT_PLAYER_BUFFERING)
        localReceiverIntentFilter.addAction(C.INTENT_PLAYER_PLAYING)

    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume")

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(localReceiver, localReceiverIntentFilter)
    }

    fun buttonPlayStopOnClick (view: View) {
        when (playerStatus) {
            C.PLAYER_STATUS_STOPPED -> {
                startService(Intent(this, TestService::class.java))
            }
            C.PLAYER_STATUS_BUFFERING -> {

            }
            C.PLAYER_STATUS_PLAYING -> {
                // saata mainactivityst teenusesse broadcast, tuleb teha broadcast listener panna teenuses käima ja teistpidi kirjutada asja väljasaatmine
                //kõik mis mainactivitis filtrit ...
            }
        }

    }

    fun updateUi(){
        when (playerStatus) {
            C.PLAYER_STATUS_STOPPED -> {
                buttonPlayStop.setText("PLAY")
            }
            C.PLAYER_STATUS_BUFFERING -> {
                buttonPlayStop.setText("BUFFERING")
            }
            C.PLAYER_STATUS_PLAYING -> {
                buttonPlayStop.setText("STOP")
            }
        }
    }
    // dont forget "inner" to access parent class instance from inner class
    private inner class BroadcastReceiverInMainActivity: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "BroadcastReceiverInMainActivity " + intent?.action)

            when (intent?.action){
                null -> {}
                C.INTENT_SERVICE_TIME ->
                    textViewTitle.text = intent.getStringExtra("time")
                C.INTENT_PLAYER_STOPPED -> {
                    playerStatus = C.PLAYER_STATUS_STOPPED
                }
                C.INTENT_PLAYER_BUFFERING -> {
                    playerStatus = C.PLAYER_STATUS_BUFFERING
                }
                C.INTENT_PLAYER_PLAYING -> {
                    playerStatus = C.PLAYER_STATUS_PLAYING
                }
            }
            updateUi()
        }
    }
}
