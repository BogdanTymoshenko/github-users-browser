package com.amicablesoft.ghusersbrowser.android.ui.search

import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.amicablesoft.ghusersbrowser.android.repository.UsersRepository
import com.amicablesoft.ghusersbrowser.android.utils.ImmediateSchedulerRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Inject




/**
 * Created by bogdan on 4/1/17.
 */
class UserSearchPresenterTest {
    @Rule
    @JvmField
    var immediateSchedulerRule = ImmediateSchedulerRule()

    @Mock lateinit var view: UserSearchView
    @Mock lateinit var repository: UsersRepository

    @Inject lateinit var presenter: UserSearchPresenter

    lateinit var queryTextChangeEvents: PublishSubject<SearchQueryEvent>
    private val query = "octocat"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        queryTextChangeEvents = PublishSubject.create()

        whenever(view.queryTextChangeEvents).thenReturn(queryTextChangeEvents)
        val component = DaggerUserSearchTestComponent.builder()
            .usersRepositoryModule(UsersRepositoryTestModule(repository))
            .build()
        component.inject(this)

        presenter.view = view
    }

    @Test
    fun searchUsers_empty() {
        whenever(repository.search(query)).thenReturn(Observable.just(listOf()))

        presenter.onStart()
        queryTextChangeEvents.onNext(SearchQueryEvent("octocat", false))

        Mockito.verify(view).showUsers(listOf())
    }

    @Test
    fun searchUsers_found() {
        val user = UserShort(
            id=1,
            login="octocat",
            avatarUrl="http://example.com/img/octocat.png")

        whenever(repository.search(query)).thenReturn(Observable.just(listOf(user)))

        presenter.onStart()
        queryTextChangeEvents.onNext(SearchQueryEvent("octocat", false))

        Mockito.verify(view).showUsers(listOf(user))
    }

    @Test
    fun searchUsers_error() {
        val error = Exception()
        whenever(repository.search(query)).thenReturn(Observable.error(error))

        presenter.onStart()
        queryTextChangeEvents.onNext(SearchQueryEvent("octocat", false))

        Mockito.verify(view).showError(Mockito.any(Throwable::class.java))
        Mockito.verify(view, never()).showUsers(any())
    }
}

