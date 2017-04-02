package com.amicablesoft.ghusersbrowser.android.repository.repos

import com.amicablesoft.ghusersbrowser.android.model.Repo
import rx.Observable

/**
 * Created by bogdan on 4/2/17.
 */
interface ReposRepository {
    fun repos(forUserLogin:String): Observable<List<Repo>>
}
