package io.github.devcavin.netinsight.dto;

import io.github.devcavin.netinsight.enums.IpStatus;

import java.util.Map;

public record IpInfo(
        Map<String, IpPair> localIps,
        IpPair publicIp,
        IpStatus status,
        String message
) {
    public record IpPair(
            String ipv4,
            String ipv6
    ) {}
}