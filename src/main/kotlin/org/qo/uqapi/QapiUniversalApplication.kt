package org.qo.uqapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.concurrent.ScheduledExecutorService

@SpringBootApplication
class QapiUniversalApplication

fun main(args: Array<String>) {
    runApplication<QapiUniversalApplication>(*args)
}
