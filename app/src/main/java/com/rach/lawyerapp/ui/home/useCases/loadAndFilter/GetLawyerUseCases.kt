package com.rach.lawyerapp.ui.home.useCases.loadAndFilter

import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.home.data.repo.LawyerRepository
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLawyerUseCases @Inject constructor(
    private val lawyerRepository: LawyerRepository
) {

    operator fun invoke(): Flow<Response<List<LawyerModel>>>{
        return lawyerRepository.getLawyers()
    }
}