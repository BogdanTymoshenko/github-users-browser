package com.amicablesoft.ghusersbrowser.android.repository.users

import com.amicablesoft.ghusersbrowser.android.api.UsersApi
import com.amicablesoft.ghusersbrowser.android.api.errors.ConnectionError
import com.amicablesoft.ghusersbrowser.android.api.errors.LimitExceededError
import com.amicablesoft.ghusersbrowser.android.api.errors.NotFoundError
import com.amicablesoft.ghusersbrowser.android.model.User
import com.amicablesoft.ghusersbrowser.android.utils.ImmediateSchedulerRule
import com.amicablesoft.ghusersbrowser.android.utils.buildLimitException
import com.amicablesoft.ghusersbrowser.android.utils.buildNotFoundException
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.observers.TestSubscriber
import java.io.IOException
import java.util.*

/**
 * Created by bogdan on 4/1/17.
 */
class UserFetchRepositoryTest {
    @Rule
    @JvmField
    var immediateSchedulerRule = ImmediateSchedulerRule()

    @Mock lateinit var usersApi: UsersApi
    lateinit var repository: UsersRepository

    private val userLogin = "octocat"

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        val instance = UsersRepositoryImpl()
        instance.usersApi = usersApi
        repository = instance
    }

    @Test fun user_found() {
        val user = User(
            id=1,
            login="octocat",
            name="Octocat",
            avatarUrl="http://example.com/img/octocat.png",
            company="GitHub",
            location="San Francisco",
            bio="Social coding",
            email="octocat@github.com",
            blog="https://github.com/blog",
            following = 2,
            publicRepos = 3,
            followers = 4)

        whenever(usersApi.user(eq(userLogin))).thenReturn(Observable.just(user))

        val subscriber = TestSubscriber<User>()
        repository.userBy(userLogin).subscribe(subscriber)

        val foundUser = subscriber.onNextEvents.first()
        assertEquals(user, foundUser)
    }

    @Test fun user_notFound() {
        val notFound = buildNotFoundException()
        whenever(usersApi.user(eq(userLogin))).thenReturn(Observable.error(notFound))

        val subscriber = TestSubscriber<User>()
        repository.userBy(userLogin).subscribe(subscriber)

        subscriber.assertError(NotFoundError::class.java)
    }

    @Test fun user_limitExceededErrorWithResetDate() {
        val resetTs = 1491136467
        val resetDate = Date(resetTs * 1000L)
        val limitError = buildLimitException(resetDate)
        whenever(usersApi.user(eq(userLogin))).thenReturn(Observable.error(limitError))

        val subscriber = TestSubscriber<User>()
        repository.userBy(userLogin).subscribe(subscriber)

        val expectedError = LimitExceededError(limitError, resetDate)
        subscriber.assertError(expectedError)
    }

    @Test fun user_limitExceededError() {
        val limitError = buildLimitException(null)
        whenever(usersApi.user(eq(userLogin))).thenReturn(Observable.error(limitError))

        val subscriber = TestSubscriber<User>()
        repository.userBy(userLogin).subscribe(subscriber)

        val expectedError = LimitExceededError(limitError, null)
        subscriber.assertError(expectedError)
    }

    @Test fun user_ConnectionError() {
        val connectionError = IOException()
        whenever(usersApi.user(eq(userLogin))).thenReturn(Observable.error(connectionError))

        val subscriber = TestSubscriber<User>()
        repository.userBy(userLogin).subscribe(subscriber)

        subscriber.assertError(ConnectionError::class.java)
    }

    @Test fun user_UnexpectedError() {
        val error = Exception()
        whenever(usersApi.user(eq(userLogin))).thenReturn(Observable.error(error))

        val subscriber = TestSubscriber<User>()
        repository.userBy(userLogin).subscribe(subscriber)

        subscriber.assertError(Exception::class.java)
    }
}
