package org.qo.uqapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QapiUniversalApplication

fun main(args: Array<String>) {
    runApplication<QapiUniversalApplication>(*args)
}
