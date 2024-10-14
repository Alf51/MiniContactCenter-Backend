package ru.golden.alf.litlepms.security

import org.apache.tomcat.websocket.AuthenticationException
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import ru.golden.alf.litlepms.services.PersonServices


//todo грокнуть
@Component
class JwtChannelInterceptor(private val jwtUtils: JWTUtils, private val personServices: PersonServices) :
    ChannelInterceptor {


    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        if (SecurityContextHolder.getContext().authentication != null) {
            return message
        }

        val accessor = StompHeaderAccessor.wrap(message)
        val authToken = accessor.getFirstNativeHeader("Authorization")?.substring(7)
        val contextToken = SecurityContextHolder.getContext().authentication
        if (authToken != null && jwtUtils.validateTokenAndGetLogin(authToken).isNotEmpty()) {
            //todo Токен валидный, аутентификация прошла успешно
            val login = jwtUtils.validateTokenAndGetLogin(authToken)
            val userDetails: UserDetails = personServices.loadUserByUsername(login)
            val authToken =
                UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)

            SecurityContextHolder.getContext().authentication = authToken

        } else {
            throw AuthenticationException("Invalid JWT Token")
        }
        return message
    }
}