package com.amicablesoft.ghusersbrowser.android.ui.repos

import com.amicablesoft.ghusersbrowser.android.repository.repos.ReposRepositoryModule
import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepositoryModule
import dagger.Module

/**
 * Created by bogdan on 4/1/17.
 */

@Module(includes = arrayOf(
        UsersRepositoryModule::class,
        ReposRepositoryModule::class))
open class UserReposModule
