package io.github.devcavin.netinsight.domain

import io.github.devcavin.netinsight.dto.response.IpResponse
import org.springframework.stereotype.Component
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.NetworkInterface

@Component
class LocalIpProvider {

    fun getLocalIps(): Map<String, IpResponse.IpPair> {
        val result = linkedMapOf<String, IpResponse.IpPair>()

        val interfaces = NetworkInterface.getNetworkInterfaces()

        while (interfaces.hasMoreElements()) {
            val ni = interfaces.nextElement()
            if (!ni.isUp || ni.isLoopback) continue

            var ipv4: String? = null
            var ipv6: String? = null

            val addresses = ni.inetAddresses
            while (addresses.hasMoreElements()) {
                val addr = addresses.nextElement()
                if (addr.isLoopbackAddress) continue

                val ip = addr.hostAddress.substringBefore('%')

                when (addr) {
                    is Inet4Address -> if (ipv4 == null) ipv4 = ip
                    is Inet6Address -> if (ipv6 == null) ipv6 = ip
                    else -> {}
                }

                if (ipv4 != null && ipv6 != null) break
            }

            if (ipv4 != null || ipv6 != null) {
                result[ni.name] = IpResponse.IpPair(ipv4, ipv6)
            }
        }

        return result
    }
}