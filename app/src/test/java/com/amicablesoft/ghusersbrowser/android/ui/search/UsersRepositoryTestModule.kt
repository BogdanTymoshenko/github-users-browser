package com.amicablesoft.ghusersbrowser.android.ui.search

import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepository
import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepositoryImpl
import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepositoryModule

/**
 * Created by bogdan on 4/1/17.
 */
class UsersRepositoryTestModule(val repository: UsersRepository): UsersRepositoryModule() {

    override fun repository(impl: UsersRepositoryImpl): UsersRepository = repository
}