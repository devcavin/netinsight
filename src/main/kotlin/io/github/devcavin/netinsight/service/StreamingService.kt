package io.github.devcavin.netinsight.service

import io.github.devcavin.netinsight.dto.BandwidthStreamEvent
import io.github.devcavin.netinsight.enum.Status
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Service
class StreamingService(
    private val bandwidthService: BandwidthService
) {
    fun streamBandwidth(
        emitter: SseEmitter,
        durationInSeconds: Long,
        intervalMillis: Long
    ) {
        Thread {
            try {
                val startTime = System.currentTimeMillis()

                while (System.currentTimeMillis() - startTime < durationInSeconds * 1000L) {
                    val bandwidth = bandwidthService.getBandwidth()

                    val event = BandwidthStreamEvent(
                        timestamp = System.currentTimeMillis(),
                        interfaceName = bandwidth.interfaceName,
                        uploadMbps = bandwidth.uploadSpeedMbps,
                        downloadMbps = bandwidth.downloadRateMbps,
                        status = bandwidth.status
                    )

                    emitter.send(event)

                    if (bandwidth.status == Status.ERROR) break

                    Thread.sleep(intervalMillis)
                }

                emitter.complete()

            } catch (e: Exception) {
                emitter.completeWithError(e)
            }
        }
            .start()
    }
}