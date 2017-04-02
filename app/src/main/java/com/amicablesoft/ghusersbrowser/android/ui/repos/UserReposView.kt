package com.amicablesoft.ghusersbrowser.android.ui.repos

import com.amicablesoft.ghusersbrowser.android.model.Repo

interface UserReposView {
    fun showAvatar(url: String)
    fun showName(value: String)
    fun showFollowersCount(value:String)
    fun showFollowingCount(value:String)
    fun showCompanyAndLocation(value: String?)

    fun showRepos(repos:List<Repo>)

    fun showRepoView(repo: Repo)

    fun showReposLoading()
    fun dismissReposLoading()

    fun showError(error: Throwable?)
}