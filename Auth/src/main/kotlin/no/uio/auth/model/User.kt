package no.uio.smol.auth.no.uio.auth.model

//import org.springframework.beans.factory.annotation.Value
import no.uio.smol.auth.no.uio.auth.config.ArgonConfig
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

    private val username: String? = user
    private val email: String? = e_mail
    private val password: String? = pass

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