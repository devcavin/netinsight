package io.github.devcavin.netinsight.domain

import io.github.devcavin.netinsight.config.IpApiProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@Component
class PublicIpClient(
    private val client: HttpClient,
    private val props: IpApiProperties
) {

    private val log = LoggerFactory.getLogger(PublicIpClient::class.java)

    fun getIpv4(): String? =
        fetch(props.ipv4Url)

    fun getIpv6(): String? =
        fetch(props.ipv6Url)

    private fun fetch(url: String): String? {
        return runCatching {
            val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() in 200..299) {
                response.body().trim()
            } else {
                log.warn("Non-200 response from $url: ${response.statusCode()}")
                null
            }
        }.onFailure {
            log.warn("Failed to call $url: ${it.message}")
        }.getOrNull()
    }
}