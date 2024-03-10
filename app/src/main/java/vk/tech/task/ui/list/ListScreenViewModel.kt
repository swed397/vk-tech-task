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
//            is ListUiEvent.SearchByQuery -> searchByQuery(event.query)
        }
    }

//    private fun searchByQuery(query: String) {
//        when (val currentState = _state.value) {
//            is ListScreenUiState.Content -> {
//                viewModelScope.launch {
//                    runSuspendCatching(
//                        action = {
//                            _state.value = currentState.copy(loadingNewPage = true)
//                            networkRepo.getProductsPagingBySearchQuery(query = query)
//                        },
//                        onSuccess = { data ->
//                            _state.value =
//                                currentState.copy(items = uiMapper(data), query = query.lowercase())
//                        },
//                        onError = { _state.value = ListScreenUiState.Error }
//                    )
//                }
//            }
//
//            ListScreenUiState.Error -> {}
//            ListScreenUiState.Loading -> {}
//        }
//    }

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
//                            if (currentState.query.isNullOrEmpty()) {
                            networkRepo.getNewPage(skipValue = currentState.items.size.toLong())
//                            } else {
//                                networkRepo.getNewPage(
//                                    skipValue = currentState.items.size.toLong(),
//                                    query = currentState.query
//                                )
//                            }
                        },
                        onSuccess = { data ->
                            _state.value = currentState.copy(items = uiMapper(data))
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
                action = { networkRepo.getAllProductsPaging() },
                onSuccess = { data ->
                    _state.value = ListScreenUiState.Content(items = uiMapper(data))
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