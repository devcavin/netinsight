package io.github.devcavin.netinsight.controller

import io.github.devcavin.netinsight.dto.InterfaceResponse
import io.github.devcavin.netinsight.service.InterfaceStatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/network")
class InterfaceStatisticsController(
    private val interfaceStatisticsService: InterfaceStatisticsService
) {
    @GetMapping("/interfaces")
    fun getInterfaceStatistics(): ResponseEntity<InterfaceResponse> {
        val response = interfaceStatisticsService.getInterfacesStats()

        return ResponseEntity.ok(response)
    }
}