package ee.itcollege.hkuusk.intentdemo2019f

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private val localReceiver = BroadcastReceiverInMainActivity()
    private val localReceiverIntentFilter: IntentFilter = IntentFilter()
    private var playerStatus = C.PLAYER_STATUS_STOPPED

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val URL = "http://dad.akaver.com/api/SongTitles/ROCKFM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localReceiverIntentFilter.addAction(C.INTENT_SERVICE_DATA)
        localReceiverIntentFilter.addAction(C.INTENT_PLAYER_STOPPED)
        localReceiverIntentFilter.addAction(C.INTENT_PLAYER_BUFFERING)
        localReceiverIntentFilter.addAction(C.INTENT_PLAYER_PLAYING)
        localReceiverIntentFilter.addAction(C.INTENT_PHONE_IS_IDLE)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf<String>(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE
                )
                // requestcode - this will be sent back,
                // when user reacts to permission request
                requestPermissions(permissions, 999)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume")

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(localReceiver, localReceiverIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver)

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
    }

    fun buttonPlayStopOnClick(view: View) {
        Log.d(TAG, "buttonPlayStopClicked")

        val handler = WebApiSingletonServiceHandler.getInstance(this)
        val httpRequest = StringRequest(
            Request.Method.GET,
            URL,
            Response.Listener<String> { response ->
                Log.d(TAG, response)
                val jsonObjectStationInfo = JSONObject(response)
                val channel = jsonObjectStationInfo.getString("StationName")
                val jsonArraySongHistoryList = jsonObjectStationInfo.getJSONArray("SongHistoryList")
                val jsonSongInfo = jsonArraySongHistoryList.getJSONObject(0)

                val artist = jsonSongInfo.getString("Artist")
                val title = jsonSongInfo.getString("Title")

                textViewArtist.text = artist
                textViewTitle.text = title
                textViewChannel.text = channel
            },
            Response.ErrorListener { }
        )

        handler.addToRequestQueue(httpRequest)

        when (playerStatus) {
            C.PLAYER_STATUS_STOPPED -> {
                startService(Intent(this, MusicService::class.java))
            }
            C.PLAYER_STATUS_BUFFERING -> {

            }
            C.PLAYER_STATUS_PLAYING -> {
                LocalBroadcastManager
                    .getInstance(applicationContext)
                    .sendBroadcast(Intent(C.INTENT_UI_STOP))
            }
        }
    }

    fun updateUi() {
        when (playerStatus) {
            C.PLAYER_STATUS_STOPPED -> {
                imageButtonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
            C.PLAYER_STATUS_BUFFERING -> {
                imageButtonPlayPause.setImageResource(R.drawable.ic_autorenew_black_24dp)
            }
            C.PLAYER_STATUS_PLAYING -> {
                imageButtonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
            }
        }
    }

    // dont forget "inner" to access parent class instance from inner class
    private inner class BroadcastReceiverInMainActivity : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "BroadcastReceiverInMainActivity " + intent?.action)

            when (intent?.action) {
                null -> {
                }
                C.INTENT_SERVICE_DATA -> {
                    textViewTitle.text = intent.getStringExtra("textViewTitle")
                    textViewChannel.text = intent.getStringExtra("textViewChannel")
                    textViewArtist.text = intent.getStringExtra("textViewArtist")
                }

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
