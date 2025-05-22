package com.rach.lawyerapp.ui.home.data.repo

import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow

interface LawyerRepository {
    fun getLawyers(): Flow<Response<List<LawyerModel>>>
    fun searchLawyers(searchText: String): Flow<Response<List<LawyerModel>>>
    fun filterLawyers(category: List<String>): Flow<Response<List<LawyerModel>>>
}