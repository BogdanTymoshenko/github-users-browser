package com.amicablesoft.ghusersbrowser.android.repository

import com.amicablesoft.ghusersbrowser.android.model.UserShort
import rx.Observable

interface UsersRepository {
    fun search(query:CharSequence): Observable<List<UserShort>>
}

