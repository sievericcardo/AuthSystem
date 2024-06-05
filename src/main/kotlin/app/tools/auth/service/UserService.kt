package app.tools.auth.service

import app.tools.auth.config.ArgonConfig
import app.tools.auth.model.User
import app.tools.auth.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val argonConfig: ArgonConfig
) {
    val allUsers: List<Any?>
        get() = userRepository.findAll()

    fun getUserById(userId: String): Optional<User?> {
        return userRepository.findById(userId)
    }

    fun createUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(userId: String) {
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
        return passwordEncoder.matches(rawPassword, user.password)
    }
}