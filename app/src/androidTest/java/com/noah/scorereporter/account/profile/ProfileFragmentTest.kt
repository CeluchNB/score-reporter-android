package com.noah.scorereporter.account.profile

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.noah.scorereporter.R
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.fake.AndroidFakeUserRepository
import com.noah.scorereporter.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@MediumTest
class ProfileFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: IUserProfileRepository

    @Before
    fun setUpRepository() {
        hiltRule.inject()
    }

    @Test
    fun testInitialView() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        (repository as AndroidFakeUserRepository).valid = true
        launchFragmentInHiltContainer<ProfileFragment>(
            navController = navController,
            navGraph = R.navigation.account_navigation
        )

        val context = ApplicationProvider.getApplicationContext<HiltTestApplication>()
        val logout = context.resources.getString(R.string.logout)
        val email = context.resources.getString(R.string.email_caps)
        val name = context.resources.getString(R.string.name_caps)
        val teams = context.resources.getString(R.string.teams_caps)

        onView(withId(R.id.button_logout))
            .check(matches(withText(logout)))

        onView(withId(R.id.email_title))
            .check(matches(withText(email)))

        onView(withId(R.id.name_title))
            .check(matches(withText(name)))

        onView(withId(R.id.teams_title))
            .check(matches(withText(teams)))
    }

    @Test
    fun testLogoutProgressBar() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        (repository as AndroidFakeUserRepository).valid = true
        launchFragmentInHiltContainer<ProfileFragment>(
            navController = navController,
            navGraph = R.navigation.account_navigation
        )

        onView(withId(R.id.button_logout)).perform(click())

        onView(withId(R.id.button_logout)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.logout_progress)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}