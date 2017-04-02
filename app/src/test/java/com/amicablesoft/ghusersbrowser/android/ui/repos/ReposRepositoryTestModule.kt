package com.amicablesoft.ghusersbrowser.android.ui.repos

import com.amicablesoft.ghusersbrowser.android.repository.repos.ReposRepository
import com.amicablesoft.ghusersbrowser.android.repository.repos.ReposRepositoryImpl
import com.amicablesoft.ghusersbrowser.android.repository.repos.ReposRepositoryModule

/**
 * Created by bogdan on 4/3/17.
 */
class ReposRepositoryTestModule(val repository: ReposRepository): ReposRepositoryModule() {

    override fun repository(impl: ReposRepositoryImpl): ReposRepository = repository
}