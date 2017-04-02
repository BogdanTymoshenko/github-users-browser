package com.amicablesoft.ghusersbrowser.android.repository.users

import com.amicablesoft.ghusersbrowser.android.api.ApiFactory
import com.amicablesoft.ghusersbrowser.android.api.ApiModule
import com.amicablesoft.ghusersbrowser.android.api.SearchApi
import com.amicablesoft.ghusersbrowser.android.api.UsersApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by bogdan on 4/1/17.
 */
@Module(includes = arrayOf(ApiModule::class))
open class UsersRepositoryModule {

    @Provides
    @Singleton
    fun searchApi(apiFactory: ApiFactory): SearchApi = apiFactory.create(SearchApi::class.java)

    @Provides
    @Singleton
    fun usersApi(apiFactory: ApiFactory): UsersApi = apiFactory.create(UsersApi::class.java)

    @Provides
    @Singleton
    open fun repository(impl: UsersRepositoryImpl): UsersRepository = impl
}
