package no.uio.auth.service

import no.uio.auth.config.ArgonConfig
import no.uio.auth.model.User
import no.uio.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val argonConfig: ArgonConfig) {
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

    fun passwordMatches(user: User, rawPassword: String): Boolean {
        val passwordEncoder = Argon2PasswordEncoder(
            argonConfig.saltLength,
            argonConfig.hashLength,
            argonConfig.parallelism,
            argonConfig.memory,
            argonConfig.iterations
        )
        return passwordEncoder.matches(rawPassword, user.getPassword())
    }
}