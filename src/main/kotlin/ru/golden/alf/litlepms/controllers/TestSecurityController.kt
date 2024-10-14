package ru.golden.alf.litlepms.controllers

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import ru.golden.alf.litlepms.dto.PersonDto
import ru.golden.alf.litlepms.repositories.PersonRepository
import ru.golden.alf.litlepms.security.JWTUtils


@RestController
@CrossOrigin(origins = ["http://localhost:3000"])
class TestSecurityController(
    private val jwtUtils: JWTUtils,
    private val authenticationManager: AuthenticationManager,
    private val personRepository: PersonRepository
) {

    //todo криво. передалай
    @PostMapping("/login")
    fun testEndpoint2(@RequestBody personDto: PersonDto): String {
        val person = personRepository.findByLogin(personDto.login)

        if (person === null) {
            return "Incorrect credentials!"
        }

        val authToken =
            UsernamePasswordAuthenticationToken(personDto.login, personDto.password)

        try {
            authenticationManager.authenticate(authToken);
        } catch (e: BadCredentialsException) {
            //todo временный вывод
            return "Incorrect credentials!"
        }

        val token = jwtUtils.generatedToken(person.login!!)

        return token
    }
}