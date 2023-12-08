package no.uio.smol.auth.no.uio.auth.controller

import no.uio.smol.auth.no.uio.auth.config.ArgonConfig
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import no.uio.smol.auth.no.uio.auth.service.UserService
import no.uio.smol.auth.no.uio.auth.model.User

data class UserRequest(val username: String, val email: String, val password: String)
data class SignUpRequest(val userRequest: UserRequest, val passwordCheck: String)
data class SignInRequest(val userRequest: UserRequest)

@RestController
@RequestMapping("/api")
class HomeController(private val argonConfig: ArgonConfig, private val userService: UserService) {

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<String> {
        if (signUpRequest.userRequest.password != signUpRequest.passwordCheck) {
            return ResponseEntity.badRequest().body("Passwords do not match")
        }

        val passwordEncoder = Argon2PasswordEncoder(
            argonConfig.saltLength,
            argonConfig.hashLength,
            argonConfig.parallelism,
            argonConfig.memory,
            argonConfig.iterations
        )
        val encryptedPassword = passwordEncoder.encode(signUpRequest.userRequest.password)

        val user = User(argonConfig, signUpRequest.userRequest.username, signUpRequest.userRequest.email, encryptedPassword)
        userService.createUser(user)

        return ResponseEntity.ok("User created successfully")
    }

    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignInRequest): ResponseEntity<String> {
        val user = userService.findByUsername(signInRequest.userRequest.username)
            ?: return ResponseEntity.badRequest().body("User not found")

        if (!user.passwordMatches(signInRequest.userRequest.password)) {
            return ResponseEntity.badRequest().body("Invalid password")
        }

        return ResponseEntity.ok("User logged in successfully")
    }
}