package app.tools.auth.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User(
    private var username: String? = null,
    private var email: String? = null,
    private var password: String? = null
) {
    @Id
    private val uuid: String = UUID.randomUUID().toString()

    fun getPassword(): String? {
        return this.password
    }
}