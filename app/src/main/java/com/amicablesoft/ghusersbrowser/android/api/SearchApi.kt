package com.amicablesoft.ghusersbrowser.android.api

import com.amicablesoft.ghusersbrowser.android.api.dto.UserSearchResultDto
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by bogdan on 4/1/17.
 */
interface SearchApi {
    @GET("search/users")
    fun users(@Query("q", encoded = true) query: String, @Query("type") type: String): Observable<UserSearchResultDto>
}