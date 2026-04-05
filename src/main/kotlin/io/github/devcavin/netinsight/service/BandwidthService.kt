package io.github.devcavin.netinsight.service

import io.github.devcavin.netinsight.domain.CounterService
import io.github.devcavin.netinsight.domain.InterfaceProvision
import io.github.devcavin.netinsight.dto.response.BandwidthResponse
import io.github.devcavin.netinsight.enum.Status
import org.springframework.stereotype.Service

@Service
class BandwidthService (
    private val interfaceProvision: InterfaceProvision,
    private val counterService: CounterService
) {

    fun getBandwidth(): BandwidthResponse {
        val activeInterface = interfaceProvision.getActiveInterface() ?: return BandwidthResponse(
            status = Status.ERROR,
            message = "No active network interface detected"
        )

        val counters = counterService.getInterfaceCounters(activeInterface)

        return BandwidthResponse(
            interfaceName = activeInterface,
            uploadSpeedMbps = counters.uploadRateMbps,
            downloadRateMbps = counters.downloadRateMbps,
            totalSentMB = counters.totalSentMB,
            totalReceivedMB = counters.totalReceivedMB,
            status = Status.SUCCESS,
            message = "Active network interface detected: $activeInterface"
        )
    }
}