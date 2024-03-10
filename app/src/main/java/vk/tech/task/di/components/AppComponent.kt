package vk.tech.task.di.components

import dagger.Component
import vk.tech.task.di.modules.NetworkModule
import vk.tech.task.di.modules.NetworkRepoModule
import vk.tech.task.ui.details.DetailsViewModel
import vk.tech.task.ui.list.ListScreenViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, NetworkRepoModule::class])
interface AppComponent {
    val listViewModelFactory: ListScreenViewModel.Factory
    val detailsViewModelFactory: DetailsViewModel.Factory
}