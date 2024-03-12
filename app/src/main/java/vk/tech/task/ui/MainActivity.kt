package vk.tech.task.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import vk.tech.task.ui.details.DetailsScreenHolder
import vk.tech.task.ui.list.ListScreenHolder
import vk.tech.task.ui.nav.Routes
import vk.tech.task.ui.theme.VktechtaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VktechtaskTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Routes.LIST.name
                    ) {
                        composable(route = Routes.LIST.name) {
                            ListScreenHolder(navController = navController)
                        }

                        composable(
                            route = "${Routes.DETAILS.name}?productId={productId}",
                            arguments = listOf(navArgument("productId") { defaultValue = 0 })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId")!!.toLong()
                            DetailsScreenHolder(
                                productId = productId,
                                onNavigateBack = navController::popBackStack
                            )
                        }
                    }
                }
            }
        }
    }
}