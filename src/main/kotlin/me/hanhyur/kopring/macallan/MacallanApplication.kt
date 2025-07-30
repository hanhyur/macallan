package me.hanhyur.kopring.macallan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class MacallanApplication

fun main(args: Array<String>) {
    runApplication<MacallanApplication>(*args)
}
