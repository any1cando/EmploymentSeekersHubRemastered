package com.example.employmentseekershubremastered.model.dto.entry.point

data class UserRegistrationRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val userRole: String
)
