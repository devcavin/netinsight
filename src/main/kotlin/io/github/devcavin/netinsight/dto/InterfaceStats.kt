package io.github.devcavin.netinsight.dto

data class InterfaceStats(
    val name: String,
    val status: String,
    val macAddress: String?,
    val mtu: Int,
    val rxBytes: Long,
    val txBytes: Long,
    val rxPackets: Long,
    val txPackets: Long,
    val rxErrors: Long,
    val txErrors: Long,
    val rxDropped: Long,
    val txDropped: Long
)