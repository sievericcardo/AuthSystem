package app.tools.auth.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = arrayOf("app.tools.auth"))
open class AppConfig {
}
