package vk.tech.task.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vk.tech.task.App
import vk.tech.task.di.injectedViewModel
import vk.tech.task.domain.model.ProductModel
import vk.tech.task.ui.details.components.BackNav
import vk.tech.task.ui.details.components.ImageViewer
import vk.tech.task.ui.details.components.ProductDetail
import vk.tech.task.ui.list.ListUiEvent
import vk.tech.task.ui.theme.ErrorScreen

@Composable
fun DetailsScreenHolder(productId: Long, onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current.applicationContext
    val viewModel = injectedViewModel {
        (context as App).appComponent.detailsViewModelFactory.create(productId = productId)
    }

    val state by viewModel.state.collectAsState()
    DetailsScreen(
        state = state,
        obtainEvent = viewModel::obtainEvent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun DetailsScreen(
    state: DetailsScreenUiState,
    obtainEvent: (DetailsScreenUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    when (state) {
        is DetailsScreenUiState.Content -> MainScreen(
            productState = state,
            onNavigateBack = onNavigateBack,
            onClickItem = { obtainEvent.invoke(DetailsScreenUiEvent.ShowNewImage(itemIndex = it)) }
        )

        is DetailsScreenUiState.Error -> ErrorScreen(onClickButton = {
            obtainEvent(
                DetailsScreenUiEvent.Reload
            )
        })

        is DetailsScreenUiState.Loading -> LinearProgressIndicator()
    }
}

@Composable
private fun MainScreen(
    productState: DetailsScreenUiState.Content,
    onClickItem: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        BackNav(onNavigateBack = onNavigateBack)

        Text(
            text = productState.item.title,
            color = Color.Black,
            fontSize = 35.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        ImageViewer(
            imagesUrlList = productState.item.imagesList,
            showItemIndex = productState.showImageItem,
            onClickItem = onClickItem
        )
        Text(
            text = productState.item.description,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
        ProductDetail(title = "Цена", content = productState.item.price.toString())
        ProductDetail(
            title = "Скидка",
            content = productState.item.discountPercentage.toString()
        )
        ProductDetail(title = "Рейтинг", content = productState.item.rating.toString())
        ProductDetail(title = "Бренд", content = productState.item.brand)
        ProductDetail(title = "Категория", content = productState.item.category)
    }
}

@Composable
@Preview(showBackground = true)
private fun DetailsScreenContentPreview() {
    val item = ProductModel(
        id = 1,
        title = "Test",
        description = "Test",
        price = 0,
        discountPercentage = 0.0,
        rating = 0.0,
        brand = "brand",
        category = "category",
        imgUrl = "",
        imagesList = listOf(),
    )
    DetailsScreen(
        state = DetailsScreenUiState.Content(item = item, showImageItem = 0),
        obtainEvent = {},
        onNavigateBack = {}
    )
}

@Composable
@Preview(showBackground = true)
private fun DetailsScreenLoadingPreview() {
    DetailsScreen(state = DetailsScreenUiState.Loading, obtainEvent = {}, onNavigateBack = {})
}

@Composable
@Preview(showBackground = true)
private fun DetailsScreenErrorPreview() {
    DetailsScreen(state = DetailsScreenUiState.Error, obtainEvent = {}, onNavigateBack = {})
}