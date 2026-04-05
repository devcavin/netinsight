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

        val interfaces = NetworkInterface.getNetworkInterfaces() ?: return emptyMap()

        while (interfaces.hasMoreElements()) {
            val ni = interfaces.nextElement()

            // 🔹 Improved filtering
            if (!ni.isUp || ni.isLoopback || ni.isVirtual) continue

            val name = buildInterfaceName(ni)

            var ipv4: String? = null
            var ipv6: String? = null

            val addresses = ni.inetAddresses
            while (addresses.hasMoreElements()) {
                val addr = addresses.nextElement()

                if (addr.isLoopbackAddress) continue

                val ip = addr.hostAddress.substringBefore('%')

                if (addr is Inet4Address) {
                    // Prefer first valid IPv4
                    if (ipv4 == null) ipv4 = ip
                }
                else if (addr is Inet6Address) {
                    // Prefer non-link-local IPv6 if possible
                    if (ipv6 == null || addr.isLinkLocalAddress.not()) {
                        ipv6 = ip
                    }
                }
            }

            if (ipv4 != null || ipv6 != null) {
                result[name] = IpResponse.IpPair(ipv4, ipv6)
            }
        }

        return result
    }

    private fun buildInterfaceName(ni: NetworkInterface): String {
        val display = ni.displayName?.trim().orEmpty()
        val name = ni.name

        return if (display.isNotEmpty() && display != name) {
            "$display ($name)"
        } else {
            name
        }
    }
}