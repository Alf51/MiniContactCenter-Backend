package ru.golden.alf.litlepms.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.golden.alf.litlepms.security.JWTFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtFilter: JWTFilter) {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/test2", "/login", "/h2-console/**", "/ws", "/app/**").permitAll()
                    .anyRequest().authenticated()
            }
            // Отключаем заголовок X-Frame-Options, чтобы разрешить отображение фреймов для H2 Console
//            .headers().frameOptions().disable().and()
//            .oauth2ResourceServer { oauth2 ->
//                oauth2.jwt { jwt ->
//                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
//                }
//            }
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            //Это означает, что текущая страница может быть встроена в фреймы на страницах,
            // которые имеют тот же источник (протокол, домен, порт), что и страница, содержащая код фрейма.
            // Это стандартная настройка для предотвращения атак через фреймы от внешних источников,
            // сохраняя при этом функциональность внутри одного и того же домена.
            //Нужно для того, чтобы работала консоль h2 в браузере
            .headers {headers -> headers.frameOptions {frameOptions -> frameOptions.sameOrigin()}}

        return http.build()
    }

//    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
//        val converter = JwtAuthenticationConverter()
//        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
//        return converter
//    }

    //    fun jwtGrantedAuthoritiesConverter(): JwtGrantedAuthoritiesConverter {
//        val converter = JwtGrantedAuthoritiesConverter()
//        converter.setAuthorityPrefix("")
//        converter.setAuthoritiesClaimName("roles")
//        return converter
//    }
    @Bean
    //AuthenticationManager: Этот бин позволяет  управлять аутентификацией в приложении. Он используется для проверки учетных данных пользователя.
    //AuthenticationConfiguration: Spring предоставляет этот объект для настройки аутентификации.
    // Он содержит предопределенные конфигурации, которые могут быть использованы для получения AuthenticationManager.
    // Spring Security автоматически предоставляет этот объект (AuthenticationConfiguration)
    @Throws(Exception::class)
    fun authenticationManagerBean(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager;
    }

    @Bean
    fun getPassword(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
