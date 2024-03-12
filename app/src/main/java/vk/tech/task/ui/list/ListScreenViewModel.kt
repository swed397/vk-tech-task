package vk.tech.task.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vk.tech.task.domain.usecase.AddNewPageUsecase
import vk.tech.task.domain.repo.NetworkRepo
import vk.tech.task.domain.usecase.SearchByQueryUsecase
import vk.tech.task.domain.usecase.SelectCategoryUsecase
import vk.tech.task.ui.nav.Routes
import vk.tech.task.util.runSuspendCatching

class ListScreenViewModel @AssistedInject constructor(
    private val networkRepo: NetworkRepo,
    private val uiMapper: ListUiMapper,
    private val addNewPageUsecase: AddNewPageUsecase,
    private val searchByQueryUsecase: SearchByQueryUsecase,
    private val selectCategoryUsecase: SelectCategoryUsecase,
) : ViewModel() {

    private val _state = MutableStateFlow<ListScreenUiState>(ListScreenUiState.Loading)
    val state: StateFlow<ListScreenUiState> = _state

    private val _actions = MutableSharedFlow<ListScreenUiAction>()
    val actions: Flow<ListScreenUiAction> = _actions

    init {
        init()
    }

    fun obtainEvent(event: ListUiEvent) {
        when (event) {
            is ListUiEvent.AddPageInData -> addNewPage()
            is ListUiEvent.NavigateToProductDetails -> navigateToProductDetails(productId = event.productId)
            is ListUiEvent.SearchByQuery -> searchByQuery(event.query)
            is ListUiEvent.SelectCategory -> selectCategory(event.category)
            is ListUiEvent.Reload -> init()
        }
    }

    private fun selectCategory(category: String) {
        when (val currentState = _state.value) {
            is ListScreenUiState.Content -> {
                viewModelScope.launch {
                    runSuspendCatching(
                        action = {
                            _state.value = ListScreenUiState.Loading
                            selectCategoryUsecase(
                                repo = networkRepo,
                                currentState = currentState,
                                category = category
                            )
                        },
                        onSuccess = { data ->
                            _state.value =
                                currentState.copy(
                                    items = uiMapper(data.first),
                                    selectedCategory = if (data.second.firstOrNull { it.selected } != null) category else null,
                                    categoriesChips = data.second.sortedByDescending { it.selected }
                                )
                        },
                        onError = { _state.value = ListScreenUiState.Error }
                    )
                }
            }

            is ListScreenUiState.Error -> {}
            is ListScreenUiState.Loading -> {}
        }
    }

    private fun searchByQuery(query: String) {
        when (val currentState = _state.value) {
            is ListScreenUiState.Content -> {
                viewModelScope.launch {
                    runSuspendCatching(
                        action = {
                            _state.value = ListScreenUiState.Loading
                            searchByQueryUsecase(
                                repo = networkRepo,
                                currentState = currentState,
                                query = query
                            )
                        },
                        onSuccess = { data ->
                            _state.value =
                                currentState.copy(
                                    items = uiMapper(data),
                                    query = query.lowercase()
                                )
                        },
                        onError = { _state.value = ListScreenUiState.Error }
                    )
                }
            }

            ListScreenUiState.Error -> {}
            ListScreenUiState.Loading -> {}
        }
    }

    private fun navigateToProductDetails(productId: Long) {
        viewModelScope.launch {
            _actions.emit(ListScreenUiAction.Navigate(route = "${Routes.DETAILS.name}?productId=$productId"))
        }
    }

    private fun addNewPage() {
        when (val currentState = _state.value) {
            is ListScreenUiState.Content -> {
                viewModelScope.launch {
                    runSuspendCatching(
                        action = {
                            _state.value = currentState.copy(loadingNewPage = true)
                            addNewPageUsecase(repo = networkRepo, currentState = currentState)
                        },
                        onSuccess = { data ->
                            _state.value =
                                currentState.copy(items = currentState.items + uiMapper(data))
                        },
                        onError = { _state.value = ListScreenUiState.Error }
                    )
                }
            }

            ListScreenUiState.Error -> {}
            ListScreenUiState.Loading -> {}
        }
    }


    private fun init() {
        viewModelScope.launch {
            runSuspendCatching(
                action = {
                    networkRepo.getAllProductsPaging() to networkRepo.getCategories()
                },
                onSuccess = { dataPair ->
                    _state.value = ListScreenUiState.Content(
                        items = uiMapper(dataPair.first),
                        categoriesChips = dataPair.second.map {
                            ChipsUiModel(
                                name = it,
                                selected = false
                            )
                        }
                    )
                },
                onError = { _state.value = ListScreenUiState.Error }
            )
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(): ListScreenViewModel
    }
}