package io.github.devcavin.netinsight.dto

import io.github.devcavin.netinsight.enum.Status

data class BandwidthStreamEvent(
    val timestamp: Long,
    val interfaceName: String?,
    val uploadMbps: Double?,
    val downloadMbps: Double?,
    val status: Status
)