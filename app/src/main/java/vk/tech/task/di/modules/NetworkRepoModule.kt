package vk.tech.task.di.modules

import dagger.Binds
import dagger.Module
import vk.tech.task.data.network.NetworkRepositoryImp
import vk.tech.task.domain.repo.NetworkRepo
import javax.inject.Singleton

@Module
interface NetworkRepoModule {

    @Binds
    @Singleton
    fun bindNetworkModule(networkModule: NetworkRepositoryImp): NetworkRepo
}