package com.amicablesoft.ghusersbrowser.android.ui.repos

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amicablesoft.ghusersbrowser.android.R
import com.amicablesoft.ghusersbrowser.android.api.errors.ConnectionError
import com.amicablesoft.ghusersbrowser.android.api.errors.LimitExceededError
import com.amicablesoft.ghusersbrowser.android.model.Repo
import com.amicablesoft.ghusersbrowser.android.ui.utils.ImageLoader
import com.amicablesoft.ghusersbrowser.android.ui.utils.bindView
import java.text.SimpleDateFormat
import javax.inject.Inject

class UserReposActivity : AppCompatActivity(), UserReposView {
    companion object {
        val ARG__USER_LOGIN = "arg.user_login"
    }

    @Inject lateinit var presenter: UserReposPresenter
    lateinit var imageLoader: ImageLoader

    val coordinatorLayout: CoordinatorLayout by bindView(R.id.user_repos__coordinator_layout)
    val collapsingToolbar: CollapsingToolbarLayout by bindView(R.id.user_repos__toolbar_layout)
    val toolbar: Toolbar by bindView(R.id.user_repos__toolbar)

    val avatarImage: ImageView by bindView(R.id.user_repos__image__avatar)
    val followersLabel: TextView by bindView(R.id.user_repos__label__followers)
    val followingLabel: TextView by bindView(R.id.user_repos__label__following)
    val companyAndLocationLabel: TextView by bindView(R.id.user_repos__label__company_and_location)

    val reposList: RecyclerView by bindView(R.id.user_repos__list__repos)
    val reposLoadingView: View by bindView(R.id.user_repos__repos_loading_view)

    private lateinit var reposAdapter: UserReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repos)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        reposList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reposAdapter = UserReposAdapter(this)
        reposList.adapter = reposAdapter

        imageLoader = ImageLoader(this.applicationContext)

        DaggerUserReposComponent.create().inject(this)

        val userLogin = intent.getStringExtra(ARG__USER_LOGIN)
        presenter.view = this
        presenter.userLogin = userLogin
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun showAvatar(url: String) {
        imageLoader.loadCircleImageAsync(url, avatarImage)
    }

    override fun showName(value: String) {
        collapsingToolbar.title = value
    }

    override fun showFollowersCount(value: String) {
        followersLabel.text = value
    }

    override fun showFollowingCount(value: String) {
        followingLabel.text = value
    }

    override fun showCompanyAndLocation(value: String?) {
        companyAndLocationLabel.text = value
        companyAndLocationLabel.visibility = if (value != null) View.VISIBLE else View.GONE
    }

    override fun showRepos(repos: List<Repo>) {
        reposAdapter.setRepos(repos)
    }

    override fun showReposLoading() {
        reposLoadingView.visibility = View.VISIBLE
    }

    override fun dismissReposLoading() {
        reposLoadingView.visibility = View.GONE
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
