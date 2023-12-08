package no.uio.auth.model

import no.uio.auth.config.ArgonConfig
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import javax.persistence.Entity
import javax.persistence.Id
import java.util.UUID

@Entity
class User(private val argonConfig: ArgonConfig,
           user: String?,
           e_mail: String?,
           pass: String?) {
    @Id
    private val uuid: String = UUID.randomUUID().toString()

    private var username: String? = user
    private var email: String? = e_mail
    private var password: String? = pass

    fun passwordMatches(rawPassword: String): Boolean {
        val passwordEncoder = Argon2PasswordEncoder(
            argonConfig.saltLength,
            argonConfig.hashLength,
            argonConfig.parallelism,
            argonConfig.memory,
            argonConfig.iterations
        )
        return passwordEncoder.matches(rawPassword, this.password)
    }
}