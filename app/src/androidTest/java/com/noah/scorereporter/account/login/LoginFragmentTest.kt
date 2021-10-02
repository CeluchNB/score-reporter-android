package com.noah.scorereporter.account.login

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.noah.scorereporter.R
import com.noah.scorereporter.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUpFragment() {
        hiltRule.inject()
    }

    @Test
    fun testInitialView() {
        launchFragmentInHiltContainer<LoginFragment>()

        val context = ApplicationProvider.getApplicationContext<HiltTestApplication>()
        val email = context.resources.getString(R.string.email)
        val password = context.resources.getString(R.string.password)
        val login = context.resources.getString(R.string.login)
        val signUp = context.resources.getString(R.string.new_sign_up)

        onView(withId(R.id.input_email)).check(matches(withHint(email)))
        onView(withId(R.id.input_password)).check(matches(withHint(password)))
        onView(withId(R.id.button_login)).check(matches(withText(login)))
        onView(withId(R.id.button_sign_up)).check(matches(withText(signUp)))
    }

    @Test
    fun testLoginClickClearsPasswordInput() {
        launchFragmentInHiltContainer<LoginFragment>()

        onView(withId(R.id.input_email)).perform(typeText("test@email.com"))
        onView(withId(R.id.input_password)).perform(typeText("Pass123!"))

        onView(withId(R.id.button_login)).perform(click())

        onView(withId(R.id.input_email)).check(matches(withText("test@email.com")))
        onView(withId(R.id.input_password)).check(matches(withText("")))
    }
}