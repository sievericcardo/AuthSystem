package app.tools.auth.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name="user", schema="auth_schema")
class User(
    @Column(name="username", unique = true)
    var username: String? = null,
    @Column(name="email", unique = true)
    var email: String? = null,
    @Column(name="password")
    var password: String? = null
) {
    @Id
    val uuid: String = UUID.randomUUID().toString()
}