package ru.golden.alf.litlepms.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.golden.alf.litlepms.model.Person

@Repository
interface PersonRepository : JpaRepository<Person, Int> {
    fun findByLogin(login: String): Person?
}