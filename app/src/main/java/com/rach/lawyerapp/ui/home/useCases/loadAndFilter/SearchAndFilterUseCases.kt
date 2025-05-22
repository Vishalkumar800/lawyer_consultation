package com.rach.lawyerapp.ui.home.useCases.loadAndFilter

import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.home.data.repo.LawyerRepository
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterLawyerUseCases @Inject constructor(
    private val repository: LawyerRepository
) {
    operator fun invoke(categories:List<String>):Flow<Response<List<LawyerModel>>>{
        return repository.filterLawyers(category = categories)
    }
}



class SearchLawyerUseCases @Inject constructor(
    private val repository: LawyerRepository
){
     operator fun invoke(searchText:String):Flow<Response<List<LawyerModel>>> {
        return repository.searchLawyers(searchText = searchText)
    }
}