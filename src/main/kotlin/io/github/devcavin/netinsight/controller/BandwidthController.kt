package io.github.devcavin.netinsight.controller

import io.github.devcavin.netinsight.dto.response.BandwidthResponse
import io.github.devcavin.netinsight.service.BandwidthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/network")
class BandwidthController (
    private val bandwidthService: BandwidthService
) {
    @GetMapping("/bandwidth")
    fun getBandwidth(): ResponseEntity<BandwidthResponse> {
        val response = bandwidthService.getBandwidth()
        return ResponseEntity.ok(response)
    }
}