package no.uio.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "argon2")
open class ArgonConfig(
    val saltLength: Int,
    val hashLength: Int,
    val parallelism: Int,
    val memory: Int,
    val iterations: Int
)