package com.noah.scorereporter.account.signup

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.noah.scorereporter.R
import com.noah.scorereporter.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class SignUpFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testInitialView() {
        launchFragmentInHiltContainer<SignUpFragment>()

        val context = ApplicationProvider.getApplicationContext<HiltTestApplication>()

        val first = context.resources.getString(R.string.first_name)
        val last = context.resources.getString(R.string.last_name)
        val email = context.resources.getString(R.string.email)
        val password = context.resources.getString(R.string.password)
        val create = context.resources.getString(R.string.sign_up)

        onView(withId(R.id.input_first_name)).check(matches(withHint(first)))
        onView(withId(R.id.input_last_name)).check(matches(withHint(last)))
        onView(withId(R.id.input_email)).check(matches(withHint(email)))
        onView(withId(R.id.input_password)).check(matches(withHint(password)))
        onView(withId(R.id.button_create)).check(matches(withText(create)))
    }
}