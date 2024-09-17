package com.example.employmentseekershubremastered

import androidx.lifecycle.ViewModel

class ViewModel: ViewModel() {
    var firstNameRegistration: String? = null
    var lastNameRegistration: String? = null
    var emailRegistration: String? = null
    var passwordRegistration: String? = null

    var emailAuthorization: String? = null
    var passwordAuthorization: String? = null
    val apiClient: ApiClient = ApiClient()  // ??
}