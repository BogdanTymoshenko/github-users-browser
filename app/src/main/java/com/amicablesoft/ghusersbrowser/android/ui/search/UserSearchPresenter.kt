package com.amicablesoft.ghusersbrowser.android.ui.search

import com.amicablesoft.ghusersbrowser.android.repository.UsersRepository
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserSearchPresenter @Inject constructor() {
    @Inject lateinit var repository: UsersRepository
    lateinit var view: UserSearchView

    private var subscription: Subscription? = null

    fun onStart() {
        subscription = view.queryTextChangeEvents
            .map { event ->
                event.queryText.trim()
            }
            .filter { query ->
                query.isNotEmpty()
            }
            .debounce(1250, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                view.showLoading()
            }
            .flatMap { query ->
                repository.search(query)
                    .onErrorReturn { error ->
                        view.showError(error)
                        null
                    }
            }.doOnNext {
                view.dismissLoading()
            }
            .filter { users -> users != null }
            .subscribe({ users ->
                view.showUsers(users)
            })
    }

    fun onStop() {
        subscription?.unsubscribe()
        subscription = null
    }
}

data class SearchQueryEvent(val queryText:CharSequence, val submitted:Boolean)