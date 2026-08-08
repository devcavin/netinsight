package io.github.devcavin.netinsight.infrastructure.os

import io.github.devcavin.netinsight.dto.InterfaceCounters
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import tools.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.util.concurrent.ConcurrentHashMap

@Component
class CounterService(
    private val minIntervalMillis: Long = 1000
) {
    private val logger = LoggerFactory.getLogger(CounterService::class.java)
    private val lastCounters = ConcurrentHashMap<String, InterfaceCounters>()
    private val lastTimestamps = ConcurrentHashMap<String, Long>()

    fun getInterfaceCounters(interfaceName: String): InterfaceCounters {
        val now = System.currentTimeMillis()
        val lastTime = lastTimestamps[interfaceName] ?: 0L

        if (now - lastTime < minIntervalMillis) {
            return lastCounters[interfaceName] ?: InterfaceCounters(0, 0)
        }

        val counters = fetchCounters(interfaceName)

        val previous = lastCounters[interfaceName]
        val intervalSec =
            if (previous != null) (now - lastTime) / 1000.0 else 1.0

        val uploadRate =
            if (previous != null)
                (counters.totalSent - previous.totalSent) * 8 / 1_000_000.0 / intervalSec
            else 0.0

        val downloadRate =
            if (previous != null)
                (counters.totalReceived - previous.totalReceived) * 8 / 1_000_000.0 / intervalSec
            else 0.0

        val updated = counters.copy(
            uploadRateMbps = uploadRate,
            downloadRateMbps = downloadRate
        )

        lastCounters[interfaceName] = updated
        lastTimestamps[interfaceName] = now

        return updated
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

    // =========================
    // LINUX
    // =========================

    private fun getLinuxCounters(interfaceName: String): InterfaceCounters {
        val base = "/sys/class/net/$interfaceName/statistics"

        fun read(name: String): Long {
            val file = File("$base/$name")
            return if (file.exists()) file.readText().trim().toLong() else 0
        }

        return InterfaceCounters(
            totalSent = read("tx_bytes"),
            totalReceived = read("rx_bytes"),

            txPackets = read("tx_packets"),
            rxPackets = read("rx_packets"),

            txErrors = read("tx_errors"),
            rxErrors = read("rx_errors"),

            txDropped = read("tx_dropped"),
            rxDropped = read("rx_dropped")
        )
    }

    // =========================
    // MAC
    // =========================

    private fun getMacCounters(interfaceName: String): InterfaceCounters {
        return try {
            val process = ProcessBuilder("netstat", "-ib").start()
            val output = process.inputStream.bufferedReader().readText()

            val line = output.lines()
                .firstOrNull { it.startsWith(interfaceName) }
                ?: return InterfaceCounters(0, 0)

            val t = line.split(Regex("\\s+"))

            InterfaceCounters(
                totalReceived = t.getOrNull(10)?.toLongOrNull() ?: 0,
                totalSent = t.getOrNull(11)?.toLongOrNull() ?: 0,

                rxPackets = t.getOrNull(4)?.toLongOrNull() ?: 0,
                txPackets = t.getOrNull(6)?.toLongOrNull() ?: 0,

                rxErrors = t.getOrNull(5)?.toLongOrNull() ?: 0,
                txErrors = t.getOrNull(7)?.toLongOrNull() ?: 0,

                rxDropped = t.getOrNull(9)?.toLongOrNull() ?: 0,
                txDropped = 0
            )
        } catch (e: Exception) {
            logger.info("Exception caught: ${e.message}")
            InterfaceCounters(0, 0)
        }
    }

    // =========================
    // WINDOWS
    // =========================

    private fun getWindowsCounters(interfaceName: String): InterfaceCounters {
        return try {
            val cmd = arrayOf(
                "powershell.exe",
                "-Command",
                "Get-NetAdapterStatistics -Name '$interfaceName' | ConvertTo-Json"
            )

            val output = ProcessBuilder(*cmd)
                .start()
                .inputStream
                .bufferedReader()
                .readText()

            val json = jacksonObjectMapper().readTree(output)

            InterfaceCounters(
                totalReceived = json["BytesReceived"]?.asLong() ?: 0,
                totalSent = json["BytesSent"]?.asLong() ?: 0,

                rxPackets = json["PacketsReceived"]?.asLong() ?: 0,
                txPackets = json["PacketsSent"]?.asLong() ?: 0,

                rxErrors = json["ReceiveErrors"]?.asLong() ?: 0,
                txErrors = json["OutboundErrors"]?.asLong() ?: 0,

                rxDropped = json["ReceivedDiscardedPackets"]?.asLong() ?: 0,
                txDropped = json["OutboundDiscardedPackets"]?.asLong() ?: 0
            )

        } catch (e: Exception) {
            logger.info("Exception caught: ${e.message}")
            InterfaceCounters(0, 0)
        }
    }
}