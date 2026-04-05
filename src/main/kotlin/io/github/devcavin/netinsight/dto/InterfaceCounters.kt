package io.github.devcavin.netinsight.dto

data class InterfaceCounters(
    val totalSent: Long,
    val totalReceived: Long,
    val uploadRateMbps: Double = 0.0,
    val downloadRateMbps: Double = 0.0,
    val totalSentMB: Double = totalSent / (1024.0 * 1024.0),
    val totalReceivedMB: Double = totalReceived / (1024.0 * 1024.0)
)
