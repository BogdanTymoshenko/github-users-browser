package com.amicablesoft.ghusersbrowser.android

import android.support.design.widget.CollapsingToolbarLayout
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.core.deps.guava.base.Preconditions.checkArgument
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.amicablesoft.ghusersbrowser.android.ui.repos.UserReposAdapter
import com.amicablesoft.ghusersbrowser.android.ui.search.UserSearchActivity
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by bogdan on 4/3/17.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class UserSearchUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule<UserSearchActivity>(UserSearchActivity::class.java)

    @Before
    fun before() {
        Espresso.registerIdlingResources(mActivityTestRule.activity.presenter.getCountingIdlingResource())
    }

    @Test fun userSearch_success() {
        // Click on the search icon
        onView(withId(R.id.main_action__search)).perform(click())

        // Type the text in the search field and submit the query
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("bogdanty"))

        // Check the item we are looking for is in the search result list.
        onView(withItemText("BogdanTymoshenko")).check(matches(isDisplayed()))

        // Open user view
        onView(withText("BogdanTymoshenko")).perform(click())

        // Check user name
        onView(isAssignableFrom(CollapsingToolbarLayout::class.java))
            .check(matches(withCollapsibleToolbarTitle(`is`("Bogdan Tymoshenko"))))

        // Check current project repo shows
        onView(ViewMatchers.withId(R.id.user_repos__list__repos))
            .perform(RecyclerViewActions.scrollToHolder(isRepoName("github-users-browser")))
            .check(matches(isDisplayed()))
    }

    @After
    fun after() {
        Espresso.unregisterIdlingResources(mActivityTestRule.activity.presenter.getCountingIdlingResource())
    }


    private fun withItemText(itemText: String): Matcher<View> {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View): Boolean {
                return allOf(
                    isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                    withText(itemText)).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA RV with text " + itemText)
            }
        }
    }

    private fun withCollapsibleToolbarTitle(textMatcher: Matcher<String>): Matcher<Any> {
        return object : BoundedMatcher<Any, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }

            override fun matchesSafely(toolbarLayout: CollapsingToolbarLayout): Boolean {
                return textMatcher.matches(toolbarLayout.title)
            }
        }
    }

    private fun isRepoName(repoName:String): Matcher<UserReposAdapter.RepoViewHolder> {
        return object : TypeSafeMatcher<UserReposAdapter.RepoViewHolder>() {
            override fun matchesSafely(customHolder: UserReposAdapter.RepoViewHolder): Boolean {
                return customHolder.repoName == repoName
            }

            override fun describeTo(description: Description) {
                description.appendText("item with repo name")
            }
        }
    }
}
