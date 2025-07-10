package com.example.nomlymealtracker.data.models

import com.google.firebase.Timestamp

data class AppUser(
    val fullName: String = "",
    val email: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
