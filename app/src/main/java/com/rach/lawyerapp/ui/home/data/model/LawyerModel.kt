package com.rach.lawyerapp.ui.home.data.model

import java.util.Locale.Category

//For Ui Connection
data class LawyerModel(
    val id:String? = null,
    val image: String? = null,
    val name: String,
    val lastName: String = "",
    val description:String = "",
    val education: String,
    val language: String,
    val experience: Int,
    val perMinPrice: Int,
    val category:List<String> = emptyList()
)

// For DataBase Connection
data class LawyerDataBaseModel(
    val id:String? = null,
    val image: String? = null,
    val name: String = "",
    val lastName: String = "",
    val description:String = "",
    val education: String = "",
    val language: String = "",
    val experience: Int = 0,
    val perMinPrice: Int = 0,
    val category: List<String> = emptyList()
) {
    fun toDomain() = LawyerModel(
        id=  id,
        image = image,
        name = name,
        lastName = lastName,
        description = description,
        education = education,
        language = language,
        experience = experience,
        perMinPrice = perMinPrice,
        category = category
    )
}

