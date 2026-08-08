package io.github.devcavin.netinsight.service

import io.github.devcavin.netinsight.infrastructure.os.InterfaceProvision
import io.github.devcavin.netinsight.dto.LatencyResponse
import io.github.devcavin.netinsight.enum.Status
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.net.Socket

@Service
class LatencyService(
    private val gatewayService: GatewayService,
    private val interfaceProvision: InterfaceProvision
) {
    fun getLatency(): LatencyResponse {
        val activeInterface = interfaceProvision.getActiveInterface() ?: return LatencyResponse(
            interfaceName = null,
            gatewayLatencyMs = null,
            internetLatencyMs = null,
            status = Status.ERROR,
            message = "No active network interface detected"
        )

        val gateway = gatewayService.getDefaultGateway()

        val gatewayLatency = gateway?.let { measureLatency(it, 80) }
        val internetLatency = measureLatency("1.1.1.1", 443) // using Cloudflare - switch to Google's DNS

        return LatencyResponse(
            interfaceName = activeInterface,
            gatewayLatencyMs = gatewayLatency,
            internetLatencyMs = internetLatency,
            status = Status.SUCCESS,
            message = "Latency diagnostics completed successfully"
        )
    }


    private fun measureLatency(host: String, port: Int, timeout: Int = 3000): Long? {
        return try {
            val start = System.nanoTime()

            Socket().use { it.connect(InetSocketAddress(host, port), timeout) }

            (System.nanoTime() - start) / 1_000_000
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}