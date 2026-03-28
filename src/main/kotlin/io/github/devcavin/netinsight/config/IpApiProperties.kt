package io.github.devcavin.netinsight.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ip.api")
data class IpApiProperties(
    val ipv4Url: String = "https://api.ipify.org",
    val ipv6Url: String = "https://api64.ipify.org"
)
