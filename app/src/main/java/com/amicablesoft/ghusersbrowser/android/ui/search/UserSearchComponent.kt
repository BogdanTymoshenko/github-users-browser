package com.amicablesoft.ghusersbrowser.android.ui.search

import dagger.Component
import javax.inject.Singleton

/**
 * Created by bogdan on 4/1/17.
 */
@Singleton
@Component(modules = arrayOf(UserSearchModule::class))
interface UserSearchComponent {
    fun inject(activity: UserSearchActivity)
}