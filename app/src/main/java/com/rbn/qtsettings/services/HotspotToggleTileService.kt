package com.rbn.qtsettings.services

import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.rbn.qtsettings.R
import com.rbn.qtsettings.utils.Constants
import com.rbn.qtsettings.utils.RootShellExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Quick Settings tile that starts/stops a local-only softap and routes NAT
 * for it over the mobile uplink, entirely via root shell commands. This is
 * independent of Android's own Settings-app hotspot toggle.
 */
class HotspotToggleTileService : TileService() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val servicePrefs: SharedPreferences by lazy {
        applicationContext.getSharedPreferences("hotspot_tile_service_state", MODE_PRIVATE)
    }
    private var isBusy = false

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        if (isBusy) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Toast.makeText(this, R.string.toast_hotspot_unsupported_android, Toast.LENGTH_LONG)
                .show()
            return
        }

        val turningOn = !isHotspotOn()
        val command = if (turningOn) Constants.HOTSPOT_START_COMMAND else Constants.HOTSPOT_STOP_COMMAND

        isBusy = true
        updateTile()

        serviceScope.launch {
            val result = withContext(Dispatchers.IO) { RootShellExecutor.run(command) }
            isBusy = false

            if (result.success) {
                setHotspotOn(turningOn)
                Toast.makeText(
                    this@HotspotToggleTileService,
                    if (turningOn) R.string.toast_hotspot_started else R.string.toast_hotspot_stopped,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e(
                    "HotspotToggleTile",
                    "Root command failed (exit ${result.exitCode}): ${result.output}"
                )
                Toast.makeText(
                    this@HotspotToggleTileService,
                    R.string.toast_hotspot_command_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
            updateTile()
        }
    }

    private fun isHotspotOn(): Boolean =
        servicePrefs.getBoolean(KEY_HOTSPOT_ON, false)

    private fun setHotspotOn(on: Boolean) {
        servicePrefs.edit { putBoolean(KEY_HOTSPOT_ON, on) }
    }

    private fun updateTile() {
        val tile = qsTile ?: return

        if (isBusy) {
            tile.subtitle = getString(R.string.hotspot_state_updating)
            tile.updateTile()
            return
        }

        val on = isHotspotOn()
        tile.subtitle = ""
        tile.state = if (on) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = getString(if (on) R.string.hotspot_state_on else R.string.hotspot_state_off)
        tile.icon = Icon.createWithResource(
            this,
            if (on) R.drawable.ic_hotspot_on else R.drawable.ic_hotspot_off
        )
        tile.updateTile()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        private const val KEY_HOTSPOT_ON = "hotspot_on"
    }
}
