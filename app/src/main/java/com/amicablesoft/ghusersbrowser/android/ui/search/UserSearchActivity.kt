package com.amicablesoft.ghusersbrowser.android.ui.search

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import com.amicablesoft.ghusersbrowser.android.R
import com.amicablesoft.ghusersbrowser.android.api.errors.ConnectionError
import com.amicablesoft.ghusersbrowser.android.api.errors.LimitExceededError
import com.amicablesoft.ghusersbrowser.android.model.UserShort
import com.amicablesoft.ghusersbrowser.android.ui.repos.UserReposActivity
import com.amicablesoft.ghusersbrowser.android.ui.utils.bindView
import com.jakewharton.rxbinding.support.v7.widget.queryTextChangeEvents
import rx.subjects.PublishSubject
import java.text.SimpleDateFormat
import javax.inject.Inject

class UserSearchActivity : AppCompatActivity(), UserSearchView {
    @Inject lateinit var presenter: UserSearchPresenter

    override var queryTextChangeEvents: PublishSubject<SearchQueryEvent> = PublishSubject.create()

    val coordinatorLayout: CoordinatorLayout by bindView(R.id.user_search__coordinator_layout)
    val loadingView: View by bindView(R.id.user_search__loading_view)
    val usersList: RecyclerView by bindView(R.id.user_search__result_list)

    lateinit var searchView: SearchView
    lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)
        setSupportActionBar(findViewById(R.id.user_search__toolbar) as Toolbar)

        usersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        usersList.setHasFixedSize(true)

        usersAdapter = UsersAdapter(this)
        usersAdapter.onItemClickListener = { pos, avatarImage ->
            presenter.onUserSelected(atPosition=pos, extra=avatarImage)
        }
        usersList.adapter = usersAdapter

        DaggerUserSearchComponent.create().inject(this)

        presenter.view = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.user_search, menu)
        val searchItem = menu.findItem(R.id.main_action__search)
        DrawableCompat.setTint(searchItem.icon, ResourcesCompat.getColor(resources, R.color.icons, theme))
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.queryTextChangeEvents()
            .map { SearchQueryEvent(it.queryText(), it.isSubmitted) }
            .subscribe(queryTextChangeEvents)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun showUsers(users: List<UserShort>) {
        usersAdapter.setUsers(users)
    }

    override fun showUserRepos(user:UserShort, extra: Any) {
        val showUserRepos = Intent(this, UserReposActivity::class.java)
        showUserRepos.putExtra(UserReposActivity.ARG__USER_LOGIN, user.login)

//        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, extra as View, "user_avatar_image")
//        ActivityCompat.startActivity(this, showUserRepos, options.toBundle())
        startActivity(showUserRepos)
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loadingView.visibility = View.GONE
    }

    override fun showError(error: Throwable?) {
        val errorMessage = when (error) {
            is ConnectionError -> getString(R.string.common__error__connection_problem)
            is LimitExceededError -> {
                if (error.limitResetDate != null) {
                    val formatter = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
                    val formattedResetDate = formatter.format(error.limitResetDate)
                    getString(R.string.common__error__limit_exceeded__try_after, formattedResetDate)
                }
                else
                    getString(R.string.common__error__limit_exceeded__try_later)
            }
            else -> getString(R.string.common__error__unexpected)
        }

        Snackbar.make(coordinatorLayout, errorMessage, Snackbar.LENGTH_LONG).show()
    }
}
