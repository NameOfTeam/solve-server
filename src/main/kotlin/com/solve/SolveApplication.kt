package com.solve

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class SolveApplication

fun main(args: Array<String>) {
    runApplication<SolveApplication>(*args)
}
