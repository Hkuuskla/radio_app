package ee.itcollege.hkuusk.intentdemo2019f

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.text.format.Time
import android.transition.Scene
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class TestService : Service(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnPreparedListener {


    private val scheduledExecutorService: ScheduledExecutorService?
            = Executors.newScheduledThreadPool(1)
    private val mediaPlayer = MediaPlayer()

    companion object{
        private val TAG = TestService::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setOnErrorListener(this)
        mediaPlayer.setOnPreparedListener(this)

        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        LocalBroadcastManager
            .getInstance(applicationContext)
            .sendBroadcast(Intent(C.INTENT_PLAYER_BUFFERING))

        mediaPlayer.setDataSource("http://sky.babahhcdn.com/rrap")
        mediaPlayer.prepareAsync()


        startTimerService()
        return START_STICKY
    }

    private fun startTimerService() {

        scheduledExecutorService?.scheduleAtFixedRate(
            Runnable {
                run {
                    val s : String = Date().toString()
                    Log.d(TAG, "its alive: " + s)
                    val intent = Intent(C.INTENT_SERVICE_TIME)
                    intent.putExtra("time", s)
                    LocalBroadcastManager
                        .getInstance(applicationContext)
                        .sendBroadcast(intent)

                }
            },
            0,
            5,
            TimeUnit.SECONDS
        )
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        TODO("Return the communication channel to the service.")
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
}
