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
import vk.tech.task.domain.model.ProductModel
import vk.tech.task.domain.repo.NetworkRepo
import vk.tech.task.ui.nav.Routes
import vk.tech.task.util.runSuspendCatching

class ListScreenViewModel @AssistedInject constructor(
    private val networkRepo: NetworkRepo,
    private val uiMapper: ListUiMapper
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
        }
    }

    private fun selectCategory(category: String) {
        when (val currentState = _state.value) {
            is ListScreenUiState.Content -> {
                viewModelScope.launch {
                    runSuspendCatching(
                        action = {
                            _state.value = ListScreenUiState.Loading

                            val newChipsList = currentState.categoriesChips
                                .map { it.copy(selected = if (it.name == category) it.selected.not() else false) }
                                .toMutableList()

                            val data =
                                if (newChipsList.firstOrNull { it.selected } != null)
                                    networkRepo.getProductsByCategory(category) else networkRepo.getAllProductsPaging()

                            data to newChipsList
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
                            val data = if (query.isEmpty().not())
                                networkRepo.getProductsPagingBySearchQuery(query = query.lowercase())
                            else
                                networkRepo.getAllProductsPaging()

                            if (currentState.selectedCategory != null) {
                                data.filter { it.category == currentState.selectedCategory }
                            } else {
                                data
                            }
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
                            val data = if (currentState.query.isEmpty()) {
                                networkRepo.getNewPage(skipValue = currentState.items.size.toLong())
                            } else {
                                networkRepo.getNewPage(
                                    skipValue = currentState.items.size.toLong(),
                                    query = currentState.query
                                )
                            }
                            if (currentState.selectedCategory.isNullOrEmpty().not()) {
                                data.filter { it.category == currentState.selectedCategory }
                            }
                            data
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