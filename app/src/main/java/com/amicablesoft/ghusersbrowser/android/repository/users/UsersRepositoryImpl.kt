package com.amicablesoft.ghusersbrowser.android.repository.users

import com.amicablesoft.ghusersbrowser.android.api.SearchApi
import com.amicablesoft.ghusersbrowser.android.api.UsersApi
import com.amicablesoft.ghusersbrowser.android.api.errors.mapToServiceError
import com.amicablesoft.ghusersbrowser.android.model.User
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(): UsersRepository {
    @Inject lateinit var searchApi: SearchApi
    @Inject lateinit var usersApi: UsersApi

    override fun search(query: CharSequence): Observable<List<UserShort>> {
        val usersSearchQuery = query.buildUsersSearchQuery()
        return searchApi.users(usersSearchQuery, "User")
            .map { result ->
                result.users
            }
            .onErrorReturn { error ->
                throw error.mapToServiceError()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun userBy(login: String): Observable<User> {
        return usersApi.user(login)
            .onErrorReturn { error ->
                throw error.mapToServiceError()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun CharSequence.buildUsersSearchQuery(): String {
        return "${toString()}+in:login,fullname"
    }
}
