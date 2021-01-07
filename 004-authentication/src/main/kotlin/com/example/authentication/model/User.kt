package com.example.authentication.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    val email: String = "",
    val name: String = "",
    val password: String = "",

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date = Date(),

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    var updatedAt: Date = Date()
)

