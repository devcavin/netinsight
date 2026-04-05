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
        val activeUp = getActiveLocalIp()
        val interfaces = localIpProvider.getLocalIps()

        if (activeUp != null) {
            val match = interfaces.entries.firstOrNull {
                it.value.ipv4 == activeUp || it.value.ipv6 == activeUp
            }
            if (match != null) {
                return match.key
            }
        }

        // Fallback heuristic: first interface with IPv4
        return interfaces.entries.firstOrNull { it.value.ipv4 != null || it.value.ipv6 != null }?.key
    }
}