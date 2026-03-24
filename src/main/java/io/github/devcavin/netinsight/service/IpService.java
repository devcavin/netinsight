package io.github.devcavin.netinsight.service;

import io.github.devcavin.netinsight.dto.IpInfo;
import io.github.devcavin.netinsight.enums.IpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class IpService {
    private static final Logger log = LoggerFactory.getLogger(IpService.class);

    /**
     * Retrieves local IPs for all active interfaces, using single IPv4 + IPv6 per interface.
     */
    private Map<String, IpInfo.IpPair> getLocalIps() {
        Map<String, IpInfo.IpPair> result = new LinkedHashMap<>();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback()) continue;

                String ipv4 = null;
                String ipv6 = null;

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.isLoopbackAddress()) continue;

                    String ip = addr.getHostAddress();
                    int percentIndex = ip.indexOf('%'); // remove scope for IPv6
                    if (percentIndex != -1) ip = ip.substring(0, percentIndex);

                    if (addr instanceof Inet4Address && ipv4 == null) ipv4 = ip;
                    else if (!(addr instanceof Inet4Address) && ipv6 == null) ipv6 = ip;

                    if (ipv4 != null && ipv6 != null) break;
                }

                if (ipv4 != null || ipv6 != null) {
                    result.put(ni.getName(), new IpInfo.IpPair(ipv4, ipv6));
                }
            }

        } catch (Exception e) {
            log.error("Error while retrieving local IPs", e);
            return Collections.emptyMap();
        }

        return result;
    }

    /**
     * Fetches the public IP from a given URL.
     */
    private String fetchPublicIp(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Returns IP information including local and public addresses.
     * Public IPv6 will be null if unavailable from the ISP.
     */
    public IpInfo getIpInfo() {
        try {
            Map<String, IpInfo.IpPair> localIps = getLocalIps();

            // Public IPv4
            String publicIpv4 = fetchPublicIp("https://api.ipify.org");

            // Public IPv6, null if ISP doesn't allow
            String publicIpv6 = null;
            try {
                InetAddress test = InetAddress.getByName("api64.ipify.org");
                if (test instanceof Inet6Address) {
                    publicIpv6 = fetchPublicIp("https://api64.ipify.org");
                }
            } catch (Exception ignored) {
                // ISP doesn't provide IPv6
            }

            IpInfo.IpPair publicIp = new IpInfo.IpPair(publicIpv4, publicIpv6);

            return new IpInfo(localIps, publicIp, IpStatus.SUCCESS, null);

        } catch (Exception e) {
            log.error("Failed to retrieve IP info", e);
            return new IpInfo(null, null, IpStatus.ERROR, e.getMessage());
        }
    }
}