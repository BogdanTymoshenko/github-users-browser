package com.amicablesoft.ghusersbrowser.android.repository.repos

import com.amicablesoft.ghusersbrowser.android.api.ReposApi
import com.amicablesoft.ghusersbrowser.android.api.errors.mapToServiceError
import com.amicablesoft.ghusersbrowser.android.model.Repo
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor() : ReposRepository {
    @Inject lateinit var reposApi: ReposApi

    override fun repos(forUserLogin: String): Observable<List<Repo>> {
        return reposApi.repos(forUserLogin)
            .onErrorReturn { error ->
                throw error.mapToServiceError()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}