package io.github.devcavin.netinsight.controller;

import io.github.devcavin.netinsight.dto.IpInfo;
import io.github.devcavin.netinsight.service.IpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/network")
public class IpController {
    private final IpService ipService;

    public IpController(IpService ipService) {
        this.ipService = ipService;
    }

    @GetMapping("/ip")
    public ResponseEntity<IpInfo> getIpInfo() {
        IpInfo response = ipService.getIpInfo();
        return ResponseEntity.ok(response);
    }
}
