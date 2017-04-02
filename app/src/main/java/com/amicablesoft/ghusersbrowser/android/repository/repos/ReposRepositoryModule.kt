package com.amicablesoft.ghusersbrowser.android.repository.repos

import com.amicablesoft.ghusersbrowser.android.api.ApiFactory
import com.amicablesoft.ghusersbrowser.android.api.ApiModule
import com.amicablesoft.ghusersbrowser.android.api.ReposApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by bogdan on 4/2/17.
 */
@Module(includes = arrayOf(ApiModule::class))
open class ReposRepositoryModule {

    @Provides
    @Singleton
    fun reposApi(apiFactory: ApiFactory): ReposApi = apiFactory.create(ReposApi::class.java)

    @Provides
    @Singleton
    open fun repository(impl: ReposRepositoryImpl): ReposRepository = impl
}
