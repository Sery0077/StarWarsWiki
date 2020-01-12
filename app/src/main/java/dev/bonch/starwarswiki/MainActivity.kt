package dev.bonch.starwarswiki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var navController: NavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_people,
                R.id.nav_film,
                R.id.nav_planet,
                R.id.nav_species,
                R.id.nav_species,
                R.id.nav_vehicle
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun navigateToViewItemFragmentFromPlanets(args: Bundle?) {
        navController.navigate(R.id.action_nav_planet_to_nav_view_item_fragment, args)
    }

    fun navigateToViewItemFragmentFromStarships(args: Bundle?) {
        navController.navigate(R.id.action_nav_starship_to_nav_view_item_fragment, args)
    }

    fun navigateToViewItemFragmentFromSpecies(args: Bundle?) {
        navController.navigate(R.id.action_nav_species_to_nav_view_item_fragment, args)
    }

    fun navigateToViewItemFragmentFromVehicles(args: Bundle?) {
        navController.navigate(R.id.action_nav_vehicle_to_nav_view_item_fragment, args)
    }

    fun navigateToViewItemFragmentFromFilms(args: Bundle?) {
        navController.navigate(R.id.action_nav_film_to_nav_view_item_fragment, args)
    }

    fun navigateToViewItemFragmentFromPeoples(args: Bundle?) {
        navController.navigate(R.id.action_nav_people_to_nav_view_item_fragment, args)
    }
}
