package com.amicablesoft.ghusersbrowser.android.ui.search

import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.idling.CountingIdlingResource
import com.amicablesoft.ghusersbrowser.android.BuildConfig
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepository
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UserSearchPresenter @Inject constructor() {
    @Inject lateinit var repository: UsersRepository
    lateinit var view: UserSearchView

    private val loadedUsers = ArrayList<UserShort>()
    private var subscription: Subscription? = null

    // it's for UI testing purposes
    private var idlingResource:CountingIdlingResource? = null

    fun onStart() {
        subscription = view.queryTextChangeEvents
            .map { event ->
                event.queryText.trim()
            }
            .filter { query ->
                query.isNotEmpty()
            }
            .doOnNext {
                // it's for UI testing purposes
                idlingResource?.increment()
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
            }
            .doOnNext { users ->
                view.dismissLoading()
                // it's for UI testing purposes
                if (users == null)
                    idlingResource?.decrement()
            }
            .filter { users -> users != null }
            .subscribe({ users ->
                loadedUsers.clear()
                loadedUsers.addAll(users)
                view.showUsers(users)
                idlingResource?.decrement()
            })
    }

    fun onStop() {
        subscription?.unsubscribe()
        subscription = null
    }

    fun onUserSelected(atPosition: Int, extra:Any) {
        view.showUserRepos(loadedUsers[atPosition], extra)
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        if (idlingResource == null) {
            idlingResource = CountingIdlingResource(UserSearchPresenter::class.java.simpleName!!, BuildConfig.DEBUG)
        }
        return idlingResource!!
    }
}
