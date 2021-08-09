package com.noah.scorereporter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationController = this.findNavController(R.id.fragment_nav_host)

        navigationController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupActionBarWithNavController(this, navigationController, drawerLayout)
        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.view_navigation), navigationController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationController = this.findNavController(R.id.fragment_nav_host)
        return NavigationUI.navigateUp(navigationController, drawerLayout)
    }
}