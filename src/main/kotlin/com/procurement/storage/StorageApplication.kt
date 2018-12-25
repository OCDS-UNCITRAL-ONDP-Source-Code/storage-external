package com.procurement.storage

import com.procurement.storage.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
class StorageApplication

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}
