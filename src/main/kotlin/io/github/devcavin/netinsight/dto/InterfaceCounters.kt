package io.github.devcavin.netinsight.dto

data class InterfaceCounters(
    val totalSent: Long,
    val totalReceived: Long,

    val rxPackets: Long = 0,
    val txPackets: Long = 0,

    val rxErrors: Long = 0,
    val txErrors: Long = 0,

    val rxDropped: Long = 0,
    val txDropped: Long = 0,

    val uploadRateMbps: Double = 0.0,
    val downloadRateMbps: Double = 0.0,

    val totalSentMB: Double = totalSent / (1024.0 * 1024.0),
    val totalReceivedMB: Double = totalReceived / (1024.0 * 1024.0)
)