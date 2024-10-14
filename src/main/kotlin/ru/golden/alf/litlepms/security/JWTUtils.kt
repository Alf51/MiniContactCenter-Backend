package ru.golden.alf.litlepms.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class JWTUtils {
    fun generatedToken(login: String): String {
        val dateIssuedAt = Date()
        val dateExpiresAt = Date.from(ZonedDateTime.now().plusHours(1).toInstant())

        val token = JWT.create()
            .withIssuedAt(dateIssuedAt)
            .withExpiresAt(dateExpiresAt)
            .withIssuer("Little-pms") //Издатель
            .withSubject("User details")
            .withClaim("login", login)
            .sign(Algorithm.HMAC256(SECRET))
        return token
    }

    fun validateTokenAndGetLogin(token: String): String {
        val verified = JWT.require(Algorithm.HMAC256(SECRET))
            .withIssuer("Little-pms") //Издатель
            .withSubject("User details")
            .build()

        val tokenDecoded = verified.verify(token)
        //todo мб asString ? узнать чем отличается от toString
        return tokenDecoded.getClaim("login").asString()
    }

    companion object {
        //todo переместить в файл или лучше в переменную огружения ? Ладно.. файл сгодится
        const val SECRET: String = "fsdfsa"
    }
}