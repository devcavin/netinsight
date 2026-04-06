package io.github.devcavin.netinsight.domain

import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

@Service
class InterfaceProvision( private val localIpProvider: LocalIpProvider) {
    fun getActiveLocalIp(): String? {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1000)
            socket.localAddress.hostAddress
            }
        } catch (e: SocketException) {
            null
        }
    }

    fun getActiveInterface(): String? {
        val activeUp: String? = try {
            getActiveLocalIp()  // Try to get the active IP normally
        } catch (e: java.net.SocketTimeoutException) {
            println("SocketTimeoutException caught: ${e.message}")
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
        return java.net.NetworkInterface.getNetworkInterfaces()
            .toList()
            .flatMap { it.inetAddresses.toList() }
            .firstOrNull { !it.isLoopbackAddress && it is java.net.Inet4Address }
            ?.hostAddress ?: "127.0.0.1"
    }
}