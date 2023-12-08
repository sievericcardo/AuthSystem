package no.uio.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import no.uio.auth.model.User

interface UserRepository : JpaRepository<User?, Long?> {
    fun findByUsername(username: String?): User?
    fun findByEmail(email: String?): User?
}