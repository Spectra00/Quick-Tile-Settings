package com.rbn.qtsettings.utils

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Runs shell commands through `su -c`. Requires the device to be rooted and
 * the root manager (Magisk/KernelSU) to grant this app superuser access.
 */
object RootShellExecutor {

    private const val TAG = "RootShellExecutor"

    data class RootResult(val success: Boolean, val exitCode: Int, val output: String)

    fun run(command: String): RootResult {
        return try {
            val process = ProcessBuilder("su", "-c", command)
                .redirectErrorStream(true)
                .start()
            val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                Log.w(TAG, "Command failed (exit $exitCode): $command\n$output")
            }
            RootResult(exitCode == 0, exitCode, output)
        } catch (e: Exception) {
            Log.e(TAG, "Error running root command: $command", e)
            RootResult(success = false, exitCode = -1, output = e.message.orEmpty())
        }
    }
}
