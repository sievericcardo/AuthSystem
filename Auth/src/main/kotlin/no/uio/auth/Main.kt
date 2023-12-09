package no.uio.auth

import no.uio.auth.config.ArgonConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableConfigurationProperties(ArgonConfig::class)
//@EntityScan("no.uio.auth.model.User")
//@EnableJpaRepositories("no.*")
//@ComponentScan(basePackages = arrayOf("no.*"))
//@EntityScan("no.*")
open class Auth

fun main(args: Array<String>) {
    SpringApplication.run(Auth::class.java, *args)
}