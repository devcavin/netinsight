package io.github.devcavin.netinsight.controller

import io.github.devcavin.netinsight.service.StreamingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/v1/network/bandwidth")
class StreamingController(private val streamingService: StreamingService) {
    @GetMapping("/stream")
    fun streamBandwidth(
        @RequestParam(defaultValue = "5") duration: Long,
        @RequestParam(defaultValue = "1000") interval: Long
    ): ResponseEntity<SseEmitter> {
        val emitter = SseEmitter(0L)

        streamingService.streamBandwidth(
            emitter = emitter,
            durationInSeconds = duration,
            intervalMillis = interval
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(emitter)
    }
}