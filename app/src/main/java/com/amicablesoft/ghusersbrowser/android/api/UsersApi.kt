package com.amicablesoft.ghusersbrowser.android.api

import com.amicablesoft.ghusersbrowser.android.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface UsersApi {
    @GET("users/{login}")
    fun user(@Path("login")login:String): Observable<User>
}
