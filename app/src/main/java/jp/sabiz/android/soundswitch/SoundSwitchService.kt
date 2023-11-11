package jp.sabiz.android.soundswitch

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.media.AudioManager
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log

class SoundSwitchService : TileService() {

    private lateinit var mAudioSwitcher: AudioSwitcher
    private val mAudioStateReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if(qsTile.state != Tile.STATE_ACTIVE) {
                return
            }
            when(intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> receiveBluetoothStateChanged(intent)
                AudioManager.ACTION_AUDIO_BECOMING_NOISY -> receiveAudioBecomingNoisy()
            }
        }

        fun receiveAudioBecomingNoisy() {
            mAudioSwitcher.reVibe()
        }

        fun receiveBluetoothStateChanged(intent: Intent) {
            when(intent.extras?.getInt(BluetoothAdapter.EXTRA_STATE) ?: 0) {
                BluetoothAdapter.STATE_OFF, BluetoothAdapter.STATE_ON -> {
                    mAudioSwitcher.reVibe()
                }
            }
        }

        fun register(context: Context) {
            context.registerReceiver(this, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
            context.registerReceiver(this, IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY))
        }

        fun unregister(context: Context) {
            try {
                context.unregisterReceiver(this)
            } catch (ex: java.lang.IllegalArgumentException) {
                // ignore
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mAudioSwitcher = AudioSwitcher(this)
    }

    override fun onClick() {
        when (qsTile.state) {
            Tile.STATE_ACTIVE -> toInactive()
            Tile.STATE_INACTIVE -> toActive()
            else -> {
            }
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        toInactive()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileResource()

    }

    private fun toActive() {
        val tile = qsTile
        tile.state = Tile.STATE_ACTIVE
        mAudioStateReceiver.register(this)
        updateTileResource()
        mAudioSwitcher.vibe()
    }

    private fun toInactive() {
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE
        mAudioStateReceiver.unregister(this)
        updateTileResource()
        mAudioSwitcher.normal()
    }

    private fun updateTileResource() {
        val tile = qsTile
        when (qsTile.state) {
            Tile.STATE_ACTIVE -> {
                tile.icon = Icon.createWithResource(this, R.drawable.ic_off)
                tile.label = getString(R.string.tile_label_active)
            }
            Tile.STATE_INACTIVE -> {
                tile.icon = Icon.createWithResource(this, R.drawable.ic_on)
                tile.label = getString(R.string.tile_label_inactive)
            }
            else -> {
            }
        }
        tile.updateTile()
    }
}
