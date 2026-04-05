package io.github.devcavin.netinsight.dto.response

import io.github.devcavin.netinsight.enum.Status


data class IpResponse(
    val localIp: Map<String, IpPair>?,
    val publicIp: IpPair?,
    val status: Status,
    val message: String?
) {
    data class IpPair(
        val ipv4: String?,
        val ipv6: String?,
    )
}