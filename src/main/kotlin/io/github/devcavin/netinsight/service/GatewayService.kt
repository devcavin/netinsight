package io.github.devcavin.netinsight.service

import org.springframework.stereotype.Service

@Service
class GatewayService {
    fun getDefaultGateway(): String? {
        val os = System.getProperty("os.name").lowercase()

        return when {
            os.contains("linux") -> getLinuxGateway()
            os.contains("mac") || os.contains("darwin") -> getMacGateway()
            os.contains("windows") -> getWindowsGateway()
            else -> null
        }
    }

    private fun getLinuxGateway(): String? {
        return try {
            val output = ProcessBuilder("sh", "-c", "ip route | grep default")
                .start()
                .inputStream
                .bufferedReader()
                .readText()

            output.split(" ")
                .windowed(2)
                .firstOrNull { it[0] == "via" }
                ?.get(1)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getMacGateway(): String? {
        return try {
            val output = ProcessBuilder("route", "-n", "get", "default")
                .start()
                .inputStream
                .bufferedReader()
                .readLines()

            output
                .firstOrNull { it.trim().startsWith("gateway:") }
                ?.substringAfter("gateway:")
                ?.trim()

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getWindowsGateway(): String? {
        return try {
            val output = ProcessBuilder("powershell.exe", "-Command", "(Get-Route -DestinationPrefix '0.0.0.0/0').NextHop")
                .start()
                .inputStream
                .bufferedReader()
                .readLines()

            output.firstOrNull()?.trim()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}