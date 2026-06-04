package io.github.devcavin.netinsight.controller

import io.github.devcavin.netinsight.dto.LatencyResponse
import io.github.devcavin.netinsight.service.LatencyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/network")
class LatencyController(private val latencyService: LatencyService) {
    @GetMapping("/latency")
    fun getLatency(): ResponseEntity<LatencyResponse> {
        return ResponseEntity.ok(latencyService.getLatency())
    }
}