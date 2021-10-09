package jp.sabiz.android.soundswitch


import android.content.Context
import android.media.AudioManager

class AudioSwitcher internal constructor(context: Context) {

    private val mAudioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val mStorage: DataStorage = DataStorage(context, STORAGE_NAME)

    companion object {

        private const val STORAGE_NAME = "AUDIO_SWITCHER_DATA"
        private val STREAM_TABLE = arrayOf(AudioManager.STREAM_ALARM, AudioManager.STREAM_DTMF,
                                            AudioManager.STREAM_NOTIFICATION, AudioManager.STREAM_SYSTEM,
                                            AudioManager.STREAM_VOICE_CALL,AudioManager.STREAM_MUSIC)
    }

    private fun unmute() {
        STREAM_TABLE.forEach {
            val volume = mStorage.restore(it,mAudioManager.getStreamMaxVolume(it))
            if (it == AudioManager.STREAM_VOICE_CALL) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING,volume, AudioManager.FLAG_SHOW_UI)
            }
            mAudioManager.setStreamVolume(it,volume, AudioManager.FLAG_SHOW_UI)
        }
    }

    private fun mute() {
        STREAM_TABLE.forEach {
            mStorage.store(it,mAudioManager.getStreamVolume(it))
            if (it == AudioManager.STREAM_VOICE_CALL) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_RING,0, AudioManager.FLAG_SHOW_UI)
            }
            mAudioManager.setStreamVolume(it,0, AudioManager.FLAG_SHOW_UI)
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
