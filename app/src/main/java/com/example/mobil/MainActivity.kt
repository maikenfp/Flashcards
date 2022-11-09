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
        Log.e("NAVCONTROLLER: ", "Before potential error")

        // *********************** Feilen skjer her ************************
        // Du kan kjøre getController() hvor mange gange du vil med å gå frem og tilbake mellom kortstokker
        // Men når man klikker på et kort, så stopper koden her av en eller annen grunn
        // Jeg forstår ikke hvorfor
        // *****************************************************************
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment

        Log.e("NAVCONTROLLER: ", "After potential error")
        navController = navHostFragment.navController
        Log.e("NAVCONTROLLER: ", navController.toString())
    }


    // This might be better split up into two functions
    fun navigateToFragment(refString: String, deckID: String, cardID : String,  deckTitle: String) {
        Log.e("NAVCONTROLLER", "Before getController()")
        getController()
        Log.e("NAVCONTROLLER", "After getController()")
        // Navigate to DeckFragment using ref: "toCards"
        if (refString == "toCards") {
            val directions = MainFragmentDirections.actionMainFragmentToDeckFragment(deckID, deckTitle)
            navController.navigate(directions)
        }
        // Navigate to CardFragment using ref: "toACard"
        if (refString == "toACard") {
            Log.e("NAVIGATE TO CARD", deckID)
            val directions = DeckFragmentDirections.actionDeckFragmentToCardFragment(deckID, cardID, deckTitle)
            navController.navigate(directions)
        }
    }

}


