package no.uio.auth.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = arrayOf("no.uio.auth"))
open class AppConfig {
}
