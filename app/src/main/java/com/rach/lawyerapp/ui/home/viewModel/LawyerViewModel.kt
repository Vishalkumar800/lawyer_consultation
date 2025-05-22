package com.rach.lawyerapp.ui.home.viewModel

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rach.lawyerapp.R
import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.home.useCases.loadAndFilter.FilterLawyerUseCases
import com.rach.lawyerapp.ui.home.useCases.loadAndFilter.GetLawyerUseCases
import com.rach.lawyerapp.ui.home.useCases.loadAndFilter.SearchLawyerUseCases
import com.rach.lawyerapp.utils.collectAndHandle
import com.rach.lawyerapp.utils.collectAndHandleSuspend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


//This viewmodel is used in HomeViewModel
@HiltViewModel
class LawyerViewModel @Inject constructor(
    private val getLawyerUseCases: GetLawyerUseCases,
    private val searchLawyerUseCases: SearchLawyerUseCases,
    private val filterLawyerUseCases: FilterLawyerUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(LawyerUiState())
    val uiState: StateFlow<LawyerUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var filterJob: Job? = null

    init {
        fetchLawyers()
    }

    fun onEvents(events: LawyerUiEvents) {
        when (events) {
            is LawyerUiEvents.OnSearchTextChange -> {
                _uiState.value = _uiState.value.copy(searchText = events.searchText)
                searchLawyers(events.searchText)
            }

            is LawyerUiEvents.FetchLawyers -> {
                fetchLawyers()
            }

            is LawyerUiEvents.OnFilterChange -> {
               if (events.categories.isEmpty() || events.categories.contains("All")){
                   fetchLawyers()
               }else{
                   filterLawyers(events.categories)
               }
            }
        }
    }

    private fun fetchLawyers() {

        filterJob?.cancel()
        searchJob?.cancel()

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            getLawyerUseCases().collectAndHandle(
                onError = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error?.localizedMessage ?: "Unknown Error"
                    )
                },
                loading = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                }
            ) { lawyersData ->
                _uiState.value = _uiState.value.copy(
                    lawyers = lawyersData,
                    isLoading = false,
                    isNotFound = lawyersData.isEmpty() && _uiState.value.searchText.isNotEmpty(),
                    selectedCategories = emptyList()
                )
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun searchLawyers(searchText: String) {
        searchJob?.cancel()
        filterJob?.cancel()

        if (searchText.isEmpty()) {
            fetchLawyers()
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            searchLawyerUseCases(searchText)
                .debounce(300)
                .collectAndHandle(
                    onError = { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = error?.localizedMessage ?: "Unknown Error"
                        )
                    },
                    loading = {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                ) { searchedLawyers ->
                    _uiState.value = _uiState.value.copy(
                        lawyers = searchedLawyers.toList(),
                        isLoading = false,
                        isNotFound = searchedLawyers.isEmpty() && searchText.isNotEmpty()
                    )
                }
        }
    }

    @OptIn(FlowPreview::class)
    private fun filterLawyers(categories:List<String>){
        searchJob?.cancel()
        filterJob?.cancel()

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            filterLawyerUseCases(categories).debounce(300)
                .collectAndHandle(
                    onError = {error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = error?.localizedMessage ?:"Unknown Error"
                        )
                    },
                    loading = {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }
                ) {filterLawyerData ->
                    _uiState.value = _uiState.value.copy(
                        lawyers = filterLawyerData,
                        isLoading = false,
                        errorMessage = null,
                        isNotFound = filterLawyerData.isEmpty(),
                        selectedCategories = categories
                    )
                }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        filterJob?.cancel()
        super.onCleared()
    }

}

data class LawyerUiState(
    val lawyers: List<LawyerModel> = emptyList(),
    val searchText: String = "",
    val selectedCategories:List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isNotFound: Boolean = false,
    val errorMessage: String? = null
)

sealed class LawyerUiEvents {
    data class OnSearchTextChange(val searchText: String) : LawyerUiEvents()
    data object FetchLawyers : LawyerUiEvents()
    data class OnFilterChange(val categories: List<String>):LawyerUiEvents()
}