package io.github.devcavin.netinsight.domain

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketException
import java.net.SocketTimeoutException

@Service
class InterfaceProvision( private val localIpProvider: LocalIpProvider) {
    private val logger = LoggerFactory.getLogger(InterfaceProvision::class.java)
    fun getActiveLocalIp(): String? {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1000)
            socket.localAddress.hostAddress
            }
        } catch (e: SocketException) {
            logger.info("SocketException caught: ${e.message}")
            null
        }
    }

    fun getActiveInterface(): String? {
        val activeUp: String? = try {
            getActiveLocalIp()  // Try to get the active IP normally
        } catch (e: SocketTimeoutException) {
            logger.info("SocketTimeoutException caught: ${e.message}")
            getFallbackIp()  // If timeout happens, use fallback
        }

        val interfaces = localIpProvider.getLocalIps() // All local network interfaces

        if (activeUp != null) {
            // Find the interface whose IPv4 or IPv6 matches the active IP
            val match = interfaces.entries.firstOrNull {
                it.value.ipv4 == activeUp || it.value.ipv6 == activeUp
            }
            if (match != null) {
                return match.key
            }
        }

        // Fallback heuristic: first interface with IPv4 or IPv6
        return interfaces.entries.firstOrNull { it.value.ipv4 != null || it.value.ipv6 != null }?.key
    }

    // Fallback function to return a safe IP
    fun getFallbackIp(): String {
        return NetworkInterface.getNetworkInterfaces()
            .toList()
            .flatMap { it.inetAddresses.toList() }
            .firstOrNull { !it.isLoopbackAddress && it is java.net.Inet4Address }
            ?.hostAddress ?: "127.0.0.1"
    }
}