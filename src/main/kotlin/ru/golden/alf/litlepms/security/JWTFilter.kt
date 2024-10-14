package ru.golden.alf.litlepms.security

import com.auth0.jwt.exceptions.JWTVerificationException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.golden.alf.litlepms.services.PersonServices

@Component
class JWTFilter(private val personServices: PersonServices, private val jwtUtils: JWTUtils) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)

            if (token.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный токен в заголовке")
            } else {

                try {
                    val login = jwtUtils.validateTokenAndGetLogin(token)
                    val userDetails: UserDetails = personServices.loadUserByUsername(login)
                    val authToken =
                        UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)

                    if (SecurityContextHolder.getContext().authentication == null) {
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                } catch (e: JWTVerificationException) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "недействительный токен")
                }

            }
        }
        filterChain.doFilter(request, response)
    }
}