package app.tools.auth.controller

import app.tools.auth.config.ArgonConfig
import app.tools.auth.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@TestMethodOrder(OrderAnnotation::class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var argonConfig: ArgonConfig

    @BeforeEach
    fun setup() {
        `when`(argonConfig.saltLength).thenReturn(16)
        `when`(argonConfig.hashLength).thenReturn(32)
        `when`(argonConfig.parallelism).thenReturn(2)
        `when`(argonConfig.memory).thenReturn(65536)
        `when`(argonConfig.iterations).thenReturn(2)
        `when`(argonConfig.encoding).thenReturn("base64")
    }

    @Order(1)
    @Test
    fun signUpTest_PasswordsDoNotMatch() {
        val signUpRequest = SignUpRequest(UserRequest("username", "email@test.com", "password"), "differentPassword")

        mockMvc.perform(post("/api/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest)
            .andExpect(content().string("Passwords do not match"))
    }

    @Order(2)
    @Test
    fun signUpTest() {
        val signUpRequest = SignUpRequest(UserRequest("username", "email@test.com", "password"), "password")

        mockMvc.perform(post("/api/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isOk)
    }

    @Order(3)
    @Test
    fun signInTest_InvalidUsername() {
        val signInRequest = SignInRequest(UserRequest(username="invalidUsername", password="password"))

        mockMvc.perform(post("/api/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInRequest)))
            .andExpect(status().isBadRequest)
            .andExpect(content().string("User not found"))
    }

    @Order(4)
    @Test
    fun signInTest() {
        val signInRequest = SignInRequest(UserRequest(username="username", password="password"))

        mockMvc.perform(post("/api/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInRequest)))
            .andExpect(status().isOk)
    }
}
