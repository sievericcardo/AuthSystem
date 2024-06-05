package app.tools.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import app.tools.auth.model.User

interface UserRepository : JpaRepository<User?, String?> {
    fun findByUsername(username: String?): User?
    fun findByEmail(email: String?): User?
}