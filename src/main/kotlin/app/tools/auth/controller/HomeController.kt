package app.tools.auth.controller

import app.tools.auth.config.ArgonConfig
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import app.tools.auth.service.UserService
import app.tools.auth.model.User
import app.tools.auth.service.JwtService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody
import org.apache.commons.text.StringEscapeUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

data class UserRequest(val username: String, val email: String?=null, val password: String)
data class SignUpRequest(val userRequest: UserRequest, val passwordCheck: String)
data class SignInRequest(val userRequest: UserRequest)

/**
 * The home controller handles the signup and signin endpoints.
 */
@RestController
@RequestMapping("/api")
class HomeController(private val argonConfig: ArgonConfig, private val userService: UserService, private val jwtService: JwtService) {

    private val log : Logger = LoggerFactory.getLogger(this.javaClass);

    @GetMapping("/status")
    fun status(): String {
        log.info("Application is running")
        return "Application is running"
    }

    /**
     * Sign up a new user.
     *
     * @param signUpRequest The request to sign up a new user.
     */
    @Operation(summary = "Sign up a new user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created user"),
        ApiResponse(responseCode = "401", description = "You are not authorized to create a user"),
        ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        ApiResponse(responseCode = "400", description = "Passwords do not match"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @PostMapping("/signup")
    fun signUp(@SwaggerRequestBody(description = "Request to sign up a new user") @RequestBody signUpRequest: SignUpRequest): ResponseEntity<String> {
        if (signUpRequest.userRequest.password != signUpRequest.passwordCheck) {
            return ResponseEntity.badRequest().body("Passwords do not match")
        }

        log.info("Creating user: {}", signUpRequest.userRequest)

        // Do input sanification on the request here
        val username = StringEscapeUtils.escapeHtml4(signUpRequest.userRequest.username.trim())
        val email = StringEscapeUtils.escapeHtml4(signUpRequest.userRequest.email?.trim())
        val password = StringEscapeUtils.escapeHtml4(signUpRequest.userRequest.password.trim())

        val passwordEncoder = Argon2PasswordEncoder(
            argonConfig.saltLength,
            argonConfig.hashLength,
            argonConfig.parallelism,
            argonConfig.memory,
            argonConfig.iterations
        )
        val encryptedPassword = passwordEncoder.encode(password)

        val user = User(username, email, encryptedPassword)
        userService.createUser(user)
        userService.flush()

        if (userService.findByUsername(username) == null) {
            log.error("Failed to create user: {}", user)
            return ResponseEntity.badRequest().body("Failed to create user")
        }

        log.info("User created: {}", user)

        return ResponseEntity.ok("User created successfully")
    }

    /**
     * Sign in a user.
     *
     * @param signInRequest The request to sign in a user.
     */
    @Operation(summary = "Sign in a user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully signed in user"),
        ApiResponse(responseCode = "401", description = "You are not authorized to sign in a user"),
        ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        ApiResponse(responseCode = "400", description = "Invalid username and/or password"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @PostMapping("/signin")
    fun signIn(@SwaggerRequestBody(description = "Request to sign in a new user") @RequestBody signInRequest: SignInRequest): ResponseEntity<String> {
        val username = StringEscapeUtils.escapeHtml4(signInRequest.userRequest.username.trim())
        val password = StringEscapeUtils.escapeHtml4(signInRequest.userRequest.password.trim())

        log.info("Request to sign in user: {}", username)

        val user = userService.findByUsername(username)
            ?: return ResponseEntity.badRequest().body("User not found")

        if (!userService.passwordMatches(user, password)) {
            log.warn("Invalid password for user: {}", user)
            return ResponseEntity.badRequest().body("Invalid password")
        }

        log.info("User logged in: {}", user)

        val jwtToken = jwtService.generateToken(username, user.role)
        log.info("JWT token generated: {}", jwtToken)

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $jwtToken")

        return ResponseEntity("User logged in successfully", headers, HttpStatus.OK)
    }

    @Operation(summary = "Validate token of a user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Token is valid"),
        ApiResponse(responseCode = "401", description = "Token is invalid"),
        ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
        ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
        ApiResponse(responseCode = "500", description = "Internal server error")
    ])
    @PostMapping("/validate")
    fun validateToken(@RequestHeader("Authorization") token: String): ResponseEntity<String> {
        val jwtToken = token.replace("Bearer ", "")
        if (jwtService.validateToken(jwtToken)) {
            return ResponseEntity.ok("Token is valid")
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid")
    }
}