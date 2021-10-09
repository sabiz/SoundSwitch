package jp.sabiz.android.soundswitch

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class SoundSwitchService : TileService() {

    private lateinit var mAudioSwitcher: AudioSwitcher

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
        updateTileResource()
        mAudioSwitcher.vibe()
    }

    private fun toInactive() {
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE
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
