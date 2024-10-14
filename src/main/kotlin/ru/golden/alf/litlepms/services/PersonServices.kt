package ru.golden.alf.litlepms.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.golden.alf.litlepms.repositories.PersonRepository
import ru.golden.alf.litlepms.security.PersonDetails

@Service
//todo заглушка ! Позже реализовать
class PersonServices(private val personRepository: PersonRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null) {
            throw RuntimeException("Пользователь не найден")
        }

        val result = personRepository.findByLogin(username) ?: throw RuntimeException("Пользователь не найден")

        return PersonDetails(result)
    }
}