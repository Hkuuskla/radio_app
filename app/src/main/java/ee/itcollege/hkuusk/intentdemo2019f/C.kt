package ee.itcollege.hkuusk.intentdemo2019f

class C {
    companion object {
        const val INTENT_SERVICE_TIME = "ee.itcollege.testservice.time"

        const val PLAYER_STATUS_STOPPED = 0;
        const val PLAYER_STATUS_BUFFERING = 1;
        const val PLAYER_STATUS_PLAYING = 2;
        const val PLAYER_STATUS_ONCALL = 3;


        const val INTENT_PLAYER_STOPPED = "ee.itcollege.musicservice.player.stopped";
        const val INTENT_PLAYER_BUFFERING = "ee.itcollege.musicservice.player.buffering";
        const val INTENT_PLAYER_PLAYING = "ee.itcollege.musicservice.player.playing";

        const val INTENT_UI_STOP = "ee.itcollege.musicservice.ui.stop";
        const val INTENT_PHONE_STATE = "android.intent.action.PHONE_STATE"

    }
}