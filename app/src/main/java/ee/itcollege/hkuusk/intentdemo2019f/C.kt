package ee.itcollege.hkuusk.intentdemo2019f

class C {
    companion object {
        const val INTENT_SERVICE_DATA = "ee.itcollege.testservice.data"

        const val PLAYER_STATUS_STOPPED = 0;
        const val PLAYER_STATUS_BUFFERING = 1;
        const val PLAYER_STATUS_PLAYING = 2;

        const val INTENT_PLAYER_STOPPED = "ee.itcollege.musicservice.player.stopped";
        const val INTENT_PLAYER_BUFFERING = "ee.itcollege.musicservice.player.buffering";
        const val INTENT_PLAYER_PLAYING = "ee.itcollege.musicservice.player.playing";
        const val INTENT_PHONE_IS_RINGING = "ee.itcollege.phoneeventreceiver.phoneisringing";
        const val INTENT_PHONE_IS_IDLE = "ee.itcollege.phoneeventreceiver.phoneisidle"
        const val INTENT_PHONE_CALL_RECEIVED = "ee.itcollege.phoneeventreceiver.phonecallreceived";

        const val INTENT_UI_STOP = "ee.itcollege.musicservice.ui.stop";

    }
}