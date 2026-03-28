package io.github.devcavin.netinsight.controller

import io.github.devcavin.netinsight.dto.response.IpResponse
import io.github.devcavin.netinsight.service.IpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/network")
class IpController(
    private val ipService: IpService
) {

    @GetMapping("/ip")
    fun getIpInfo(): ResponseEntity<IpResponse> {
        val response = ipService.getIpInfo()
        return ResponseEntity.ok(response)
    }
}