package com.amicablesoft.ghusersbrowser.android.repository.repos

import com.amicablesoft.ghusersbrowser.android.api.ReposApi
import com.amicablesoft.ghusersbrowser.android.api.errors.ConnectionError
import com.amicablesoft.ghusersbrowser.android.api.errors.LimitExceededError
import com.amicablesoft.ghusersbrowser.android.model.Repo
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
 * Created by bogdan on 4/2/17.
 */
class ReposRepositoryTest {
    @Rule
    @JvmField
    var immediateSchedulerRule = ImmediateSchedulerRule()

    @Mock lateinit var reposApi: ReposApi
    lateinit var repository: ReposRepository

    private val userLogin = "octocat"

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        val instance = ReposRepositoryImpl()
        instance.reposApi = reposApi
        repository = instance
    }

    @Test fun reposResult_empty() {
        whenever(reposApi.repos(eq(userLogin))).thenReturn(Observable.just(listOf()))

        val subscriber = TestSubscriber<List<Repo>>()
        repository.repos(userLogin).subscribe(subscriber)

        val foundRepos = subscriber.onNextEvents.first()
        assertTrue(foundRepos.isEmpty())
    }

    @Test fun reposResult_found() {
        val repo = Repo(1, "oc-repo", "Description", "Kotlin", 2, 3, 4, "https://github.com/octocat/oc-repo")
        whenever(reposApi.repos(eq(userLogin))).thenReturn(Observable.just(listOf(repo)))

        val subscriber = TestSubscriber<List<Repo>>()
        repository.repos(userLogin).subscribe(subscriber)

        val resultRepos = subscriber.onNextEvents.first()
        assertEquals(1, resultRepos.size)
        assertEquals(repo, resultRepos.first())
    }

    @Test fun reposResult_limitExceededErrorWithResetDate() {
        val resetTs = 1491136467
        val resetDate = Date(resetTs * 1000L)
        val limitError = buildLimitException(resetDate)
        whenever(reposApi.repos(eq(userLogin))).thenReturn(Observable.error(limitError))

        val subscriber = TestSubscriber<List<Repo>>()
        repository.repos(userLogin).subscribe(subscriber)

        val expectedError = LimitExceededError(limitError, resetDate)
        subscriber.assertError(expectedError)
    }

    @Test fun reposResult_limitExceededError() {
        val limitError = buildLimitException(null)
        whenever(reposApi.repos(eq(userLogin))).thenReturn(Observable.error(limitError))

        val subscriber = TestSubscriber<List<Repo>>()
        repository.repos(userLogin).subscribe(subscriber)

        val expectedError = LimitExceededError(limitError, null)
        subscriber.assertError(expectedError)
    }

    @Test fun reposResult_ConnectionError() {
        val connectionError = IOException()
        whenever(reposApi.repos(eq(userLogin))).thenReturn(Observable.error(connectionError))

        val subscriber = TestSubscriber<List<Repo>>()
        repository.repos(userLogin).subscribe(subscriber)

        subscriber.assertError(ConnectionError::class.java)
    }

    @Test fun reposResult_UnexpectedError() {
        val error = Exception()
        whenever(reposApi.repos(eq(userLogin))).thenReturn(Observable.error(error))

        val subscriber = TestSubscriber<List<Repo>>()
        repository.repos(userLogin).subscribe(subscriber)

        subscriber.assertError(Exception::class.java)
    }
}
