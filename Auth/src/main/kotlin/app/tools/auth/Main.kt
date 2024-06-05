package app.tools.auth

import app.tools.auth.config.ArgonConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(ArgonConfig::class)
open class Auth

fun main(args: Array<String>) {
    SpringApplication.run(Auth::class.java, *args)
}