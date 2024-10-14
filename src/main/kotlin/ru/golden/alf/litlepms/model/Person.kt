package ru.golden.alf.litlepms.model

import jakarta.persistence.*

@Entity
@Table(name = "PERSONS")
data class Person(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "login", unique = true)
    var login: String? = null,

    @Column(name = "password")
    var password: String? = null,

    @Column(name = "role")
    var role: String? = null,
) {

}


