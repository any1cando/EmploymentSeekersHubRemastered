package com.example.employmentseekershubremastered

import android.widget.Button
import androidx.lifecycle.ViewModel
import com.example.employmentseekershubremastered.model.dto.main.filters.CheckBoxDto
import com.example.employmentseekershubremastered.model.dto.main.filters.RangeDto

class ViewModel: ViewModel() {
    var firstNameRegistration: String? = null
    var lastNameRegistration: String? = null
    var emailRegistration: String? = null
    var passwordRegistration: String? = null
    var emailAuthorization: String? = null
    var passwordAuthorization: String? = null
    val apiClient: ApiClient = ApiClient()  // ??
    val selectedCheckBoxFilters: MutableMap<String, MutableList<CheckBoxDto>> = mutableMapOf()
    var selectedRangeFilter: RangeDto? = null
}