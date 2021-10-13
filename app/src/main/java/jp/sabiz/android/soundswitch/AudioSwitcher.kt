package jp.sabiz.android.soundswitch


import android.content.Context
import android.media.AudioManager

class AudioSwitcher internal constructor(context: Context) {

    private val mAudioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private fun unmute() {
        mAudioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
    }

    private fun mute() {
        mAudioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI)
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
