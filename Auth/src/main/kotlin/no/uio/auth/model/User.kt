package no.uio.auth.model

import no.uio.auth.config.ArgonConfig
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User(user: String?,
           e_mail: String?,
           pass: String?) {
    @Id
    private val uuid: String = UUID.randomUUID().toString()

    private var username: String? = user
    private var email: String? = e_mail
    private var password: String? = pass

    fun getPassword(): String? {
        return this.password
    }
}