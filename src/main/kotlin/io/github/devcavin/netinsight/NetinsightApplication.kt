package io.github.devcavin.netinsight

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NetinsightApplication

fun main(args: Array<String>) {
	runApplication<NetinsightApplication>(*args)
}
