package com.amicablesoft.ghusersbrowser.android.ui.search

import com.amicablesoft.ghusersbrowser.android.model.UserShort
import rx.Observable

interface UserSearchView {
    val queryTextChangeEvents: Observable<SearchQueryEvent>
    fun showUsers(users: List<UserShort>)

    fun showLoading()
    fun dismissLoading()
    fun showError(error: Throwable?)
}