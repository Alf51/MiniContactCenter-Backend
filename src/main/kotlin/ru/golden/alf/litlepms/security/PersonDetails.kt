package ru.golden.alf.litlepms.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.golden.alf.litlepms.model.Person

data class PersonDetails (val user: Person) : UserDetails {
    //todo временная роль, все админы
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf<SimpleGrantedAuthority>(SimpleGrantedAuthority("ADMIN"))
    }

    override fun getPassword(): String {
        return user.password!!
    }

    override fun getUsername(): String {
        return user.login!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}