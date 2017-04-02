package com.amicablesoft.ghusersbrowser.android.ui.repos

import com.amicablesoft.ghusersbrowser.android.model.Repo
import com.amicablesoft.ghusersbrowser.android.model.User
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.amicablesoft.ghusersbrowser.android.repository.repos.ReposRepository
import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepository
import rx.Subscription
import javax.inject.Inject

class UserReposPresenter @Inject constructor() {
    @Inject lateinit var usersRepository: UsersRepository
    @Inject lateinit var reposRepository: ReposRepository

    lateinit var view: UserReposView
    lateinit var userLogin: String

    private val loadedRepos = ArrayList<Repo>()
    private val subscriptions = ArrayList<Subscription>()

    fun onStart() {
        view.showReposLoading()
        val s1 = usersRepository.userBy(userLogin)
            .onErrorReturn { error ->
                view.showError(error)
                null
            }
            .filter { user -> user != null }
            .subscribe({ user ->
                showUser(user)
            })
        subscriptions.add(s1)

        val s2 = reposRepository.repos(userLogin)
            .onErrorReturn { error ->
                view.showError(error)
                null
            }
            .doOnNext {
                view.dismissReposLoading()
            }
            .filter { repos -> repos != null }
            .subscribe({ repos ->
                loadedRepos.clear()
                loadedRepos.addAll(repos)
                view.showRepos(repos)
            })
        subscriptions.add(s2)
    }

    fun onStop() {
        subscriptions.forEach { s -> s.unsubscribe() }
        subscriptions.clear()
    }

    fun onRepoSelected(atPosition: Int) {
        view.showRepoView(loadedRepos[atPosition])
    }


    private fun showUser(user: User) {
        view.showAvatar(user.avatarUrl)
        view.showName(user.name ?: user.login)
        view.showFollowersCount(user.followers.toString())
        view.showFollowingCount(user.following.toString())
        view.showCompanyAndLocation(user.companyAndLocation())
    }

    private fun User.companyAndLocation(): String {
        var companyAndLocation = ""
        if (this.company != null) {
            companyAndLocation += this.company
        }

        if (this.location != null) {
            if (companyAndLocation.isNotEmpty()) {
                companyAndLocation += "\n"
            }

            companyAndLocation += this.location
        }

        return companyAndLocation
    }
}