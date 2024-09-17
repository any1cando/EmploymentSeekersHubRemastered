package com.example.employmentseekershubremastered.model.dto.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SalaryDto(
    val amount: Int,
    val inTime: String,
    val currency: String
) : Parcelable
