package com.noah.scorereporter.page

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.noah.scorereporter.R
import com.noah.scorereporter.launchFragmentInHiltContainer
import com.noah.scorereporter.pages.season.SeasonFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class SeasonFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testInitialView() {
        launchFragmentInHiltContainer<SeasonFragment>()

        val context = ApplicationProvider.getApplicationContext<HiltTestApplication>()
        val games = context.getString(R.string.games)

        onView(withId(R.id.games_text)).check(matches(withText(games)))
        onView(withId(R.id.team_text)).check(matches(withText("")))
        onView(withId(R.id.dash_text)).check(matches(withText("-")))
    }
}