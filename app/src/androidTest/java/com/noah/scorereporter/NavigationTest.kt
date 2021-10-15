package com.noah.scorereporter

import android.view.Gravity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.noah.scorereporter.account.AccountActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NavigationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var accountActivityRule: ActivityScenarioRule<AccountActivity>
        = ActivityScenarioRule(AccountActivity::class.java)

    @Test
    fun testNavigateToAccount() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.view_navigation))
            .perform(NavigationViewActions.navigateTo(R.id.accountActivity))

        onView(withId(R.id.email_title))
            .check(matches(withText("EMAIL")))

        onView(withId(R.id.name_title))
            .check(matches(withText("NAME")))
    }

    @Test
    fun testNavigateToSearch() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.view_navigation))
            .perform(NavigationViewActions.navigateTo(R.id.mainActivity))

        onView(withId(R.id.text_temporary))
            .check(matches(withText("Search Me!")))
    }
}