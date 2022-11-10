package com.example.mobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.example.mobil.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        findViewById<Toolbar>(R.id.main_toolbar)?.let { toolbar: Toolbar ->
            setSupportActionBar(toolbar)
            title = "My Decks"
        }

        // *********************** TEST ************************
        getController()
        setupActionBarWithNavController(navController)
        // *********************** TEST ************************
    }

    // *********************** TEST ************************
    override fun onSupportNavigateUp(): Boolean {
        getController()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    // *********************** TEST ************************

    private fun getController() {
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }


    // This might be better split up into two functions
    fun navigateToFragment(refString: String, deckID: String, deckTitle: String) {
        getController()
        // Navigate to DeckFragment using ref: "toCards"
        if (refString == "toCards") {
            val directions = MainFragmentDirections.actionMainFragmentToDeckFragment(deckID, deckTitle)
            navController.navigate(directions)
        }
        // Navigate to EditFragment using ref: "toEdit"
        if (refString == "toEdit") {
            val directions = DeckFragmentDirections.actionDeckFragmentToEditFragment(deckID, deckTitle)
            navController.navigate(directions)
        }
    }

    fun navigateToCardFragment(deckID: String, cardID: String, deckTitle: String, shuffle: Boolean) {
        // Navigate to CardFragment
        Log.e("NAVIGATE TO CARD", deckID)
        val directions = DeckFragmentDirections.actionDeckFragmentToCardFragment(deckID, cardID, deckTitle, shuffle)
        navController.navigate(directions)

    }
}


