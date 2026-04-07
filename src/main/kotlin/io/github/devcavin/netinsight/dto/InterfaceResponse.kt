package io.github.devcavin.netinsight.dto

import io.github.devcavin.netinsight.enum.Status

data class InterfaceResponse(
    val interfaces: Map<String, InterfaceStats>,
    val status: Status,
    val message: String
)