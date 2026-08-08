package io.github.devcavin.netinsight.service

import io.github.devcavin.netinsight.infrastructure.os.CounterService
import io.github.devcavin.netinsight.infrastructure.os.LocalIpProvider
import io.github.devcavin.netinsight.dto.InterfaceMeta
import io.github.devcavin.netinsight.dto.InterfaceResponse
import io.github.devcavin.netinsight.dto.InterfaceStats
import io.github.devcavin.netinsight.enum.Status
import org.springframework.stereotype.Service
import java.net.NetworkInterface

@Service
class InterfaceStatisticsService(
    private val counterService: CounterService,
    private val localIpProvider: LocalIpProvider
) {

    fun getInterfaceMeta(iName: String): InterfaceMeta {
        val ni = NetworkInterface.getByName(iName) ?: return InterfaceMeta(status = "DOWN")

        val mac = ni.hardwareAddress ?.joinToString(":") { "%02X".format(it) }

        val mtu = ni.mtu
        val status = if (ni.isUp) "UP" else "DOWN"

        return InterfaceMeta(mac = mac, mtu = mtu, status = status)
    }

    fun getInterfacesStats(): InterfaceResponse {
        val interfaces = localIpProvider.getLocalIps()

        if (interfaces.isEmpty()) {
            return InterfaceResponse(
                interfaces = emptyMap(),
                status = Status.ERROR,
                message = "No network interfaces detected."
            )
        }

        val stats = interfaces.keys.associateWith { iFace ->
            val counters = counterService.getInterfaceCounters(iFace)
            val meta = getInterfaceMeta(iFace)

            InterfaceStats(
                name = iFace,
                status = meta.status,
                macAddress = meta.mac,
                mtu = meta.mtu,
                rxBytes = counters.totalReceived,
                txBytes = counters.totalSent,
                rxPackets = counters.rxPackets,
                txPackets = counters.txPackets,
                rxErrors = counters.rxErrors,
                txErrors = counters.txErrors,
                rxDropped = counters.rxDropped,
                txDropped = counters.txDropped
            )
        }

        return InterfaceResponse(
            interfaces = stats,
            status = Status.SUCCESS,
            message = "Interface statistics received successfully."
        )
    }
}