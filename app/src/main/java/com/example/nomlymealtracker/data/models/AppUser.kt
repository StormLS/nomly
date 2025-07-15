package com.example.nomlymealtracker.data.models

import com.google.firebase.Timestamp

/**
 * Class to represent a User in the App,
 * Currently only using the Full Name, their email and when the account was created
 * @property fullName: The full name of the [AppUser] being registered
 * @property email: The email associated with the [AppUser].
 * @property createdAt: When the [AppUser] was registered.
 *
 * At some point this can be expanded to include more information about the user, such as:
 * Weight, Height, Gender, Age
 * This information can then be used to add additional features to the application, such as calculating BMI and other metrics
 */
data class AppUser(
    val fullName: String = "",
    val email: String = "",
    val createdAt: Timestamp = Timestamp.now()
)