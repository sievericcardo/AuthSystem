package no.uio.smol.auth.no.uio.auth.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api")
class HomeController {
    @GetMapping("/hello")
    fun helloWorld(): String {
        return "Hello, World!"
    }
}