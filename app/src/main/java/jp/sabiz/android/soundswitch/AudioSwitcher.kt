package jp.sabiz.android.soundswitch


import android.content.Context
import android.media.AudioManager
import android.util.Log

class AudioSwitcher internal constructor(context: Context) {

    private companion object {

        private const val STORAGE_NAME = "AUDIO_SWITCHER_DATA"
        private val STREAM_TABLE = arrayOf(AudioManager.STREAM_ALARM, AudioManager.STREAM_DTMF,
            AudioManager.STREAM_NOTIFICATION, AudioManager.STREAM_SYSTEM,
            AudioManager.STREAM_VOICE_CALL,AudioManager.STREAM_MUSIC
            ,AudioManager.STREAM_RING)
    }

    private val mAudioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val mStorage: DataStorage = DataStorage(context, STORAGE_NAME)

    fun vibe() {
        for (stream in STREAM_TABLE) {
            mStorage.store(stream,mAudioManager.getStreamVolume(stream))
        }
        mAudioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI)
        mAudioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
    }

    fun reVibe() {
        mAudioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI)
        mAudioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
    }

    fun normal() {
        mAudioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
        mAudioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        for (stream in STREAM_TABLE) {
            val volume = mStorage.restore(stream,mAudioManager.getStreamMaxVolume(stream))
            mAudioManager.setStreamVolume(stream,volume, AudioManager.FLAG_SHOW_UI)
        }

    }
}
