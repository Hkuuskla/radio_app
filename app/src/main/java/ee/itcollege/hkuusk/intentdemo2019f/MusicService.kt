package ee.itcollege.hkuusk.intentdemo2019f

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MusicService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnPreparedListener {

    companion object {
        val mediaPlayer = MediaPlayer()
        private val TAG = MusicService::class.java.simpleName
    }

    private val localReceiver = BroadcastReceiverInService()
    private val localReceiverIntentFilter: IntentFilter = IntentFilter()
    private val scheduledExecutorService: ScheduledExecutorService? =
        Executors.newScheduledThreadPool(1)

    private val URL = "http://dad.akaver.com/api/SongTitles/ROCKFM"


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        localReceiverIntentFilter.addAction(C.INTENT_UI_STOP)
        localReceiverIntentFilter.addAction(C.INTENT_PHONE_STATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setDataSource("http://sky.babahhcdn.com/rck")
        mediaPlayer.prepareAsync()

        LocalBroadcastManager
            .getInstance(applicationContext)
            .sendBroadcast(Intent(C.INTENT_PLAYER_BUFFERING))


        startTimerService()
        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(localReceiver, localReceiverIntentFilter)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        TODO("Return the communication channel to the service.")
    }

    fun stopMediaPlayer(intent: Intent) {
        mediaPlayer.stop()
    }


    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "onPrepared")
        mediaPlayer.start()
        LocalBroadcastManager
            .getInstance(applicationContext)
            .sendBroadcast(Intent(C.INTENT_PLAYER_PLAYING))
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.d(TAG, "onError")
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d(TAG, "onCompletion")
    }

    private inner class BroadcastReceiverInService : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                C.INTENT_UI_STOP -> {
                    mediaPlayer.stop()
                    LocalBroadcastManager
                        .getInstance(applicationContext)
                        .sendBroadcast(Intent(C.INTENT_PLAYER_STOPPED))
                }
            }
        }
    }

    private fun startTimerService() {

        scheduledExecutorService?.scheduleAtFixedRate(
            Runnable {
                run {
                    Log.d(TAG, "startTimerService")
                    var handler = WebApiSingletonServiceHandler.getInstance(this)
                    var httpRequest = StringRequest(
                        Request.Method.GET,
                        URL,
                        Response.Listener<String> { response ->
                            Log.d(TAG, response)
                            val jsonObjectStationInfo = JSONObject(response)
                            val channel = jsonObjectStationInfo.getString("StationName")
                            val jsonArraySongHistoryList =
                                jsonObjectStationInfo.getJSONArray("SongHistoryList")
                            val jsonSongInfo = jsonArraySongHistoryList.getJSONObject(0)

                            val artist = jsonSongInfo.getString("Artist")
                            val title = jsonSongInfo.getString("Title")

                            val intent = Intent(C.INTENT_SERVICE_TIME)
                            intent.putExtra("textViewTitle", title)
                            intent.putExtra("textViewArtist", artist)
                            intent.putExtra("textViewChannel", channel)

                            LocalBroadcastManager
                                .getInstance(applicationContext)
                                .sendBroadcast(intent)

                        },
                        Response.ErrorListener { }
                    )
                    handler.addToRequestQueue(httpRequest)
                }
            },
            0,
            15,
            TimeUnit.SECONDS
        )
    }

}
