package io.github.devcavin.netinsight.dto.response

import io.github.devcavin.netinsight.enum.Status

data class BandwidthResponse(
    val interfaceName: String? = null,
    val uploadSpeedMbps: Double = 0.0,
    val downloadRateMbps: Double = 0.0,
    val totalSentMB: Double = 0.0,
    val totalReceivedMB: Double = 0.0,
    val status: Status,
    val message: String
)