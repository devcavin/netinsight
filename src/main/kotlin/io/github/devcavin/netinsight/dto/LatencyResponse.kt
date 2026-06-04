package io.github.devcavin.netinsight.dto

import io.github.devcavin.netinsight.enum.Status

data class LatencyResponse(
    val interfaceName: String?,
    val gatewayLatencyMs: Long?,
    val internetLatencyMs: Long?,
    val status: Status,
    val message: String
)
