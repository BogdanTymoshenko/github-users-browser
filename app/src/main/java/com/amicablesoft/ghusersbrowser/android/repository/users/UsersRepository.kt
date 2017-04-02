package com.amicablesoft.ghusersbrowser.android.repository.users

import com.amicablesoft.ghusersbrowser.android.model.User
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import rx.Observable

interface UsersRepository {
    fun search(query:CharSequence): Observable<List<UserShort>>
    fun userBy(login:String): Observable<User>
}

