package no.uio.smol.auth.no.uio.auth.service

import no.uio.smol.auth.no.uio.auth.model.User
import no.uio.smol.auth.no.uio.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService @Autowired constructor(private val userRepository: UserRepository) {
    val allUsers: List<Any?>
        get() = userRepository.findAll()

    fun getUserById(userId: Long): Optional<User?> {
        return userRepository.findById(userId)
    }

    fun createUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}