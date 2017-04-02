package com.amicablesoft.ghusersbrowser.android.api

import com.amicablesoft.ghusersbrowser.android.model.Repo
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

/**
 * Created by bogdan on 4/2/17.
 */
interface ReposApi {
    @GET("users/{login}/repos")
    fun repos(@Path("login")userLogin:String): Observable<List<Repo>>
}
