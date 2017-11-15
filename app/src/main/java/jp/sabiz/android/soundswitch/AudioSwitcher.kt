package jp.sabiz.android.soundswitch


import android.content.Context
import android.media.AudioManager

class AudioSwitcher internal constructor(context: Context) {

    private val mAudioManager: AudioManager
    private val mStorage: DataStorage

    companion object {

        private val STORAGE_NAME = "AUDIO_SWITCHER_DATA"
        private val STREAM_TABLE = arrayOf(AudioManager.STREAM_ALARM, AudioManager.STREAM_DTMF,
                                            AudioManager.STREAM_NOTIFICATION, AudioManager.STREAM_SYSTEM,
                                            AudioManager.STREAM_VOICE_CALL,AudioManager.STREAM_MUSIC
                                            /*,AudioManager.STREAM_RING*/)
    }

    init {
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mStorage = DataStorage(context, STORAGE_NAME)
    }

    fun unmute() {
        for (stream in STREAM_TABLE) {
            val volume = mStorage.restore(stream,mAudioManager.getStreamMaxVolume(stream))
            mAudioManager.setStreamVolume(stream,volume, AudioManager.FLAG_SHOW_UI)
        }
    }

    fun mute() {
        for (stream in STREAM_TABLE) {
            mStorage.store(stream,mAudioManager.getStreamVolume(stream))
            mAudioManager.setStreamVolume(stream,0, AudioManager.FLAG_SHOW_UI)
        }
    }

    fun vibe() {
        mute()
        mAudioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
    }

    fun normal() {
        unmute()
        mAudioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL

    }
}
