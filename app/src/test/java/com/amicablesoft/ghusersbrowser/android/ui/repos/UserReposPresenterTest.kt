package com.amicablesoft.ghusersbrowser.android.ui.repos

import com.amicablesoft.ghusersbrowser.android.api.errors.NotFoundError
import com.amicablesoft.ghusersbrowser.android.model.Repo
import com.amicablesoft.ghusersbrowser.android.model.User
import com.amicablesoft.ghusersbrowser.android.repository.repos.ReposRepository
import com.amicablesoft.ghusersbrowser.android.repository.users.UsersRepository
import com.amicablesoft.ghusersbrowser.android.ui.search.UsersRepositoryTestModule
import com.amicablesoft.ghusersbrowser.android.utils.ImmediateSchedulerRule
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import rx.Observable
import javax.inject.Inject

/**
 * Created by bogdan on 4/1/17.
 */
class UserReposPresenterTest {
    @Rule
    @JvmField
    var immediateSchedulerRule = ImmediateSchedulerRule()

    @Mock lateinit var view: UserReposView
    @Mock lateinit var usersRepository: UsersRepository
    @Mock lateinit var reposRepository: ReposRepository

    @Inject lateinit var presenter: UserReposPresenter

    private val userLogin = "octocat"
    private val user = User(
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
    private val repo = Repo(
        id = 10,
        name = "oc-repo",
        desc = "Description",
        lang = "Kotlin",
        seen = 2,
        stars = 3,
        fork = 4,
        htmlUrl = "https://github.com/octocat/oc-repo")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val component = DaggerUserReposTestComponent.builder()
            .usersRepositoryModule(UsersRepositoryTestModule(usersRepository))
            .reposRepositoryModule(ReposRepositoryTestModule(reposRepository))
            .build()
        component.inject(this)

        presenter.view = view
    }

    @Test
    fun user_loaded() {
        whenever(usersRepository.userBy(eq(userLogin))).thenReturn(Observable.just(user))
        whenever(reposRepository.repos(eq(userLogin))).thenReturn(Observable.just(listOf(repo)))

        presenter.userLogin = userLogin
        presenter.onStart()

        verify(view).showName(user.name!!)
        verify(view).showAvatar(user.avatarUrl)
        verify(view).showFollowersCount(user.followers.toString())
        verify(view).showFollowingCount(user.following.toString())
        verify(view).showCompanyAndLocation("${user.company}\n${user.location}")
        verify(view).showRepos(listOf(repo))
    }

    @Test
    fun user_error() {
        whenever(usersRepository.userBy(eq(userLogin))).thenReturn(Observable.error(NotFoundError(Error())))
        whenever(reposRepository.repos(eq(userLogin))).thenReturn(Observable.error(NotFoundError(Error())))

        presenter.userLogin = userLogin
        presenter.onStart()

        verify(view, times(2)).showError(any(NotFoundError::class.java))
    }
}

