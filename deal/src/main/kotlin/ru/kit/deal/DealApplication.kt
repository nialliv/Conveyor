package ru.kit.deal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DealApplication

fun main(args: Array<String>) {
    runApplication<DealApplication>(*args)
}
