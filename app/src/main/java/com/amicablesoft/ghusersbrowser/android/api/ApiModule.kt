package com.amicablesoft.ghusersbrowser.android.api

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by bogdan on 4/1/17.
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    fun apiServiceFactory(): ApiFactory = BackendApiFactory()
}
