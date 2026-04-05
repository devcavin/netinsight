package io.github.devcavin.netinsight.service

import io.github.devcavin.netinsight.domain.LocalIpProvider
import io.github.devcavin.netinsight.domain.PublicIpClient
import io.github.devcavin.netinsight.dto.response.IpResponse
import io.github.devcavin.netinsight.enum.Status
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.Inet6Address
import java.net.InetAddress

@Service
class IpService(
    private val localIpProvider: LocalIpProvider,
    private val publicIpClient: PublicIpClient
) {

    private val log = LoggerFactory.getLogger(IpService::class.java)

    fun getIpInfo(): IpResponse {
        // Fetch local IPs
        val localIps = try {
            val ips = localIpProvider.getLocalIps()
            ips.ifEmpty { null }
        } catch (e: Exception) {
            log.warn("Failed to fetch local IPs", e)
            null
        }

        // Fetch public IPv4
        val ipv4 = try {
            publicIpClient.getIpv4()
        } catch (e: Exception) {
            log.warn("Failed to fetch public IPv4", e)
            null
        }

        // Fetch public IPv6
        val rawIpv6 = try {
            publicIpClient.getIpv6()
        } catch (e: Exception) {
            log.warn("Failed to fetch public IPv6", e)
            null
        }

        val ipv6 = if (rawIpv6 != null && isValidIpv6(rawIpv6)) {
            rawIpv6
        } else {
            "not configured by ISP"
        }

        // Determine if any IP exists
        val hasLocalIps = !localIps.isNullOrEmpty()
        val hasPublicIp = ipv4 != null || (rawIpv6 != null && isValidIpv6(rawIpv6))

        val isOffline = !hasLocalIps && !hasPublicIp

        val status = if (isOffline) Status.ERROR else Status.SUCCESS

        val message = if (isOffline) {
            "Failed to retrieve any IP information."
        } else {
            "IP information retrieved successfully."
        }

        // Return empty maps for offline, otherwise full data
        val localIpResult = if (isOffline) emptyMap() else localIps ?: emptyMap()
        val publicIpResult = if (isOffline) IpResponse.IpPair(null, null)
        else IpResponse.IpPair(ipv4, ipv6)

        return IpResponse(
            localIp = localIpResult,
            publicIp = publicIpResult,
            status = status,
            message = message
        )
    }

    private fun isValidIpv6(ip: String): Boolean {
        return try {
            InetAddress.getByName(ip) is Inet6Address
        } catch (e: Exception) {
            false
        }
    }
}