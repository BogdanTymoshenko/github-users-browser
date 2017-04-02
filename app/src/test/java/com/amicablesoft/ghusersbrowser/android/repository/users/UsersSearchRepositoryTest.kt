package com.amicablesoft.ghusersbrowser.android.repository.users

import com.amicablesoft.ghusersbrowser.android.api.SearchApi
import com.amicablesoft.ghusersbrowser.android.api.UsersApi
import com.amicablesoft.ghusersbrowser.android.api.dto.UserSearchResultDto
import com.amicablesoft.ghusersbrowser.android.api.errors.ConnectionError
import com.amicablesoft.ghusersbrowser.android.api.errors.LimitExceededError
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.amicablesoft.ghusersbrowser.android.utils.ImmediateSchedulerRule
import com.amicablesoft.ghusersbrowser.android.utils.buildLimitException
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
class UsersSearchRepositoryTest {
    @Rule
    @JvmField
    var immediateSchedulerRule = ImmediateSchedulerRule()

    @Mock lateinit var searchApi: SearchApi
    lateinit var repository: UsersRepository

    private val type = "User"
    private val query = "octocat"
    private val buildQuery = query+"+in:login,fullname"

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        val instance = UsersRepositoryImpl()
        instance.searchApi = searchApi
        repository = instance
    }

    @Test fun searchResult_empty() {
        whenever(searchApi.users(eq(buildQuery), eq(type))).thenReturn(Observable.just(UserSearchResultDto(listOf())))

        val subscriber = TestSubscriber<List<UserShort>>()
        repository.search(query).subscribe(subscriber)

        val resultUsers = subscriber.onNextEvents.first()
        assertTrue(resultUsers.isEmpty())
    }

    @Test fun searchResult_found() {
        val user = UserShort(1, "octocat", "http://example.com/img/octocat.png")

        val searchResult = UserSearchResultDto(listOf(user))
        whenever(searchApi.users(eq(buildQuery), eq(type))).thenReturn(Observable.just(searchResult))

        val subscriber = TestSubscriber<List<UserShort>>()
        repository.search(query).subscribe(subscriber)

        val resultUsers = subscriber.onNextEvents.first()
        assertEquals(1, resultUsers.size)
        assertEquals(user, resultUsers.first())
    }

    @Test fun searchResult_limitExceededErrorWithResetDate() {
        val resetTs = 1491136467
        val resetDate = Date(resetTs * 1000L)
        val limitError = buildLimitException(resetDate)
        whenever(searchApi.users(eq(buildQuery), eq(type))).thenReturn(Observable.error(limitError))

        val subscriber = TestSubscriber<List<UserShort>>()
        repository.search(query).subscribe(subscriber)

        val expectedError = LimitExceededError(limitError, resetDate)
        subscriber.assertError(expectedError)
    }

    @Test fun searchResult_limitExceededError() {
        val limitError = buildLimitException(null)
        whenever(searchApi.users(eq(buildQuery), eq(type))).thenReturn(Observable.error(limitError))

        val subscriber = TestSubscriber<List<UserShort>>()
        repository.search(query).subscribe(subscriber)

        val expectedError = LimitExceededError(limitError, null)
        subscriber.assertError(expectedError)
    }

    @Test fun searchResult_ConnectionError() {
        val connectionError = IOException()
        whenever(searchApi.users(eq(buildQuery), eq(type))).thenReturn(Observable.error(connectionError))

        val subscriber = TestSubscriber<List<UserShort>>()
        repository.search(query).subscribe(subscriber)

        subscriber.assertError(ConnectionError::class.java)
    }

    @Test fun searchResult_UnexpectedError() {
        val error = Exception()
        whenever(searchApi.users(eq(buildQuery), eq(type))).thenReturn(Observable.error(error))

        val subscriber = TestSubscriber<List<UserShort>>()
        repository.search(query).subscribe(subscriber)

        subscriber.assertError(Exception::class.java)
    }
}
