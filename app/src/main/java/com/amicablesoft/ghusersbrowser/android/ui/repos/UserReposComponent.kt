package com.amicablesoft.ghusersbrowser.android.ui.repos

import dagger.Component
import javax.inject.Singleton

/**
 * Created by bogdan on 4/1/17.
 */
@Singleton
@Component(modules = arrayOf(UserReposModule::class))
interface UserReposComponent {
    fun inject(activity: UserReposActivity)
}