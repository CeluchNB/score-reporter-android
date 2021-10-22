package com.noah.scorereporter.page

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.noah.scorereporter.R
import com.noah.scorereporter.launchFragmentInHiltContainer
import com.noah.scorereporter.pages.team.TeamFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class TeamFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testInitialView() {
        launchFragmentInHiltContainer<TeamFragment>()

        val context = ApplicationProvider.getApplicationContext<HiltTestApplication>()
        val followText = context.getString(R.string.follow)

        onView(withId(R.id.button_follow)).check(matches(withText(followText)))
        onView(withId(R.id.button_follow)).check(matches(not(isEnabled())))
        onView(withId(R.id.team_text)).check(matches(withText("")))
        onView(withId(R.id.dash_text)).check(matches(withText("-")))
    }
}