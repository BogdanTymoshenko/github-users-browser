package com.amicablesoft.ghusersbrowser.android.ui.search

import com.amicablesoft.ghusersbrowser.android.repository.UsersRepository
import com.amicablesoft.ghusersbrowser.android.repository.UsersRepositoryImpl
import com.amicablesoft.ghusersbrowser.android.repository.UsersRepositoryModule

/**
 * Created by bogdan on 4/1/17.
 */
class UsersRepositoryTestModule(val repository:UsersRepository): UsersRepositoryModule() {

    override fun repository(impl: UsersRepositoryImpl): UsersRepository = repository
}