package com.example.employmentseekershubremastered

import androidx.lifecycle.ViewModel
import com.example.employmentseekershubremastered.model.dto.ApiClient

class EntryPointViewModel: ViewModel() {
    var firstNameRegistration: String? = null
    var lastNameRegistration: String? = null
    var emailRegistration: String? = null
    var passwordRegistration: String? = null
    var selectedRoleIdRegistration: Int? = null
    var emailAuthorization: String? = null
    var passwordAuthorization: String? = null
    val apiClient: ApiClient = ApiClient()  // ??
}