package com.example.mobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.mobil.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mobil.databinding.ActivityFlashcardcontainerBinding

class FlashcardContainer : AppCompatActivity() {

    private lateinit var mainBinding : ActivityFlashcardcontainerBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityFlashcardcontainerBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        findViewById<Toolbar>(R.id.main_toolbar)?.let { toolbar: Toolbar ->
            setSupportActionBar(toolbar)
        }

        getController()
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        getController()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun getController() {
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }

}
