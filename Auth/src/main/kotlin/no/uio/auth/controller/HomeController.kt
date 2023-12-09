package no.uio.auth.controller

import no.uio.auth.config.ArgonConfig
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import no.uio.auth.service.UserService
import no.uio.auth.model.User
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses

data class UserRequest(val username: String, val email: String, val password: String)
data class SignUpRequest(val userRequest: UserRequest, val passwordCheck: String)
data class SignInRequest(val userRequest: UserRequest)

/**
 * The home controller handles the signup and signin endpoints.
 */
@RestController
@RequestMapping("/api")
class HomeController(private val argonConfig: ArgonConfig, private val userService: UserService) {

    /**
     * Sign up a new user.
     *
     * @param signUpRequest The request to sign up a new user.
     */
    @ApiOperation(value = "Sign up a new user")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully created user"),
        ApiResponse(code = 401, message = "You are not authorized to create a user"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        ApiResponse(code = 400, message = "Passwords do not match"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/signup")
    fun signUp(@ApiParam(value = "Request to sign up a new user") @RequestBody signUpRequest: SignUpRequest): ResponseEntity<String> {
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

        val user = User(signUpRequest.userRequest.username, signUpRequest.userRequest.email, encryptedPassword)
        userService.createUser(user)

        return ResponseEntity.ok("User created successfully")
    }

    /**
     * Sign in a user.
     *
     * @param signInRequest The request to sign in a user.
     */
    @ApiOperation(value = "Sign in a user")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully signed in user"),
        ApiResponse(code = 401, message = "You are not authorized to sign in a user"),
        ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        ApiResponse(code = 400, message = "Invalid username and/or password"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @PostMapping("/signin")
    fun signIn(@ApiParam(value = "Request to sign in a new user") @RequestBody signInRequest: SignInRequest): ResponseEntity<String> {
        val user = userService.findByUsername(signInRequest.userRequest.username)
            ?: return ResponseEntity.badRequest().body("User not found")

        if (!userService.passwordMatches(user, signInRequest.userRequest.password)) {
            return ResponseEntity.badRequest().body("Invalid password")
        }

        return ResponseEntity.ok("User logged in successfully")
    }
}