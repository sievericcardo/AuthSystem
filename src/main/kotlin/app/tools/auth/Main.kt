package app.tools.auth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["app.tools.auth.config"])
open class Auth

fun main(args: Array<String>) {
    SpringApplication.run(Auth::class.java, *args)
}