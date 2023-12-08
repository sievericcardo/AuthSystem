package no.uio.smol.auth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
object YourProjectApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(YourProjectApplication::class.java, *args)
    }
}