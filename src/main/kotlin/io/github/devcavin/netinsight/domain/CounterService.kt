package io.github.devcavin.netinsight.domain

import io.github.devcavin.netinsight.dto.InterfaceCounters
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Component
class CounterService(
    private val minIntervalMillis: Long = 1000 // 1 second minimum interval
) {

    private val lastCounters = ConcurrentHashMap<String, InterfaceCounters>()
    private val lastTimestamps = ConcurrentHashMap<String, Long>()

    fun getInterfaceCounters(interfaceName: String): InterfaceCounters {
        val now = System.currentTimeMillis()
        val lastTime = lastTimestamps[interfaceName] ?: 0L

        // Return cached value if within min interval
        if (now - lastTime < minIntervalMillis) {
            return lastCounters[interfaceName] ?: InterfaceCounters(0, 0)
        }

        val counters = fetchCounters(interfaceName)

        val previous = lastCounters[interfaceName]
        val intervalSec = if (previous != null) (now - lastTime) / 1000.0 else 1.0

        val uploadRate = if (previous != null) (counters.totalSent - previous.totalSent) * 8 / 1_000_000.0 / intervalSec else 0.0
        val downloadRate = if (previous != null) (counters.totalReceived - previous.totalReceived) * 8 / 1_000_000.0 / intervalSec else 0.0

        val updatedCounters = counters.copy(uploadRateMbps = uploadRate, downloadRateMbps = downloadRate)

        lastCounters[interfaceName] = updatedCounters
        lastTimestamps[interfaceName] = now

        return updatedCounters
    }

    private fun fetchCounters(interfaceName: String): InterfaceCounters {
        val osName = System.getProperty("os.name").lowercase()
        return when {
            osName.contains("linux") -> getLinuxCounters(interfaceName)
            osName.contains("mac") || osName.contains("darwin") -> getMacCounters(interfaceName)
            osName.contains("windows") -> getWindowsCounters(interfaceName)
            else -> InterfaceCounters(0, 0)
        }
    }

    private fun getLinuxCounters(interfaceName: String): InterfaceCounters {
        val basePath = "/sys/class/net/$interfaceName/statistics"
        val rxFile = File("$basePath/rx_bytes")
        val txFile = File("$basePath/tx_bytes")
        if (!rxFile.exists() || !txFile.exists()) return InterfaceCounters(0, 0)

        val rx = rxFile.readText().trim().toLong()
        val tx = txFile.readText().trim().toLong()
        return InterfaceCounters(totalSent = tx, totalReceived = rx)
    }

    private fun getMacCounters(interfaceName: String): InterfaceCounters {
        return try {
            val process = ProcessBuilder("netstat", "-ib").start()
            val output = process.inputStream.bufferedReader().readText()
            val line = output.lines().firstOrNull { it.startsWith(interfaceName) } ?: return InterfaceCounters(0, 0)
            val tokens = line.split(Regex("\\s+"))
            val rx = tokens.getOrNull(6)?.toLongOrNull() ?: 0L
            val tx = tokens.getOrNull(9)?.toLongOrNull() ?: 0L
            InterfaceCounters(totalSent = tx, totalReceived = rx)
        } catch (e: Exception) {
            e.printStackTrace()
            InterfaceCounters(0, 0)
        }
    }

    private fun getWindowsCounters(interfaceName: String): InterfaceCounters {
        return try {
            val cmd = arrayOf(
                "powershell.exe",
                "-Command",
                "Get-NetAdapterStatistics -Name '$interfaceName' | Select-Object -ExpandProperty BytesReceived,BytesSent"
            )
            val process = ProcessBuilder(*cmd).start()
            val output = process.inputStream.bufferedReader().readLines()
            if (output.size >= 2) {
                val rx = output[0].trim().toLongOrNull() ?: 0L
                val tx = output[1].trim().toLongOrNull() ?: 0L
                InterfaceCounters(totalSent = tx, totalReceived = rx)
            } else InterfaceCounters(0, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            InterfaceCounters(0, 0)
        }
    }
}