package org.sam.store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class StoreApplication

fun main(args: Array<String>) {
    runApplication<StoreApplication>(*args)
}