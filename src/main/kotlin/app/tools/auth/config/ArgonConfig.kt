package app.tools.auth.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "argon2")
open class ArgonConfig {
    @Value("\${argon2.saltLength}")
    var saltLength: Int = 0

    @Value("\${argon2.hashLength}")
    var hashLength: Int = 0

    @Value("\${argon2.parallelism}")
    var parallelism: Int = 0

    @Value("\${argon2.memory}")
    var memory: Int = 0

    @Value("\${argon2.iterations}")
    var iterations: Int = 0

    @Value("\${argon2.encoding}")
    var encoding: String = ""
}