package com.noah.scorereporter.account.login

import android.view.Gravity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.noah.scorereporter.MainActivity
import com.noah.scorereporter.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testNavigateToLogin() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.view_navigation))
            .perform(NavigationViewActions.navigateTo(R.id.loginFragment))

        onView(withId(R.id.input_email))
            .check(matches(withHint("Email")))

        onView(withId(R.id.input_password))
            .check(matches(withHint("Password")))
    }
}