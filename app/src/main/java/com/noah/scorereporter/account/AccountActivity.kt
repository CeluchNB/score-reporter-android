package com.noah.scorereporter.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.noah.scorereporter.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationController = this.findNavController(R.id.fragment_nav_host)

        navigationController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        val topLevelDestinations = setOf(R.id.profileFragment, R.id.loginFragment)
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(drawerLayout)
            .build()

        NavigationUI.setupActionBarWithNavController(this, navigationController, appBarConfiguration)
        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.view_navigation), navigationController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navigationController = this.findNavController(R.id.fragment_nav_host)
        return NavigationUI.navigateUp(navigationController, appBarConfiguration)
    }
}