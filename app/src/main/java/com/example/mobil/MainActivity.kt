package com.example.mobil

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.navigation.fragment.NavHostFragment
import com.example.mobil.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mobil.adapter.CardsAdapter
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        findViewById<Toolbar>(R.id.main_toolbar)?.let { toolbar: Toolbar ->
            setSupportActionBar(toolbar)
            title = ""
        }
    }

    fun setToolbarVisibility() {
        findViewById<Toolbar>(R.id.main_toolbar)?.let { toolbar: Toolbar ->
            setSupportActionBar(toolbar)

            if (toolbar.isVisible) {
                toolbar.visibility = INVISIBLE
            }
            else {
                toolbar.visibility = VISIBLE
            }
        }
    }

    fun navigateToFragment(refString: String, string: String) {
        val backBtn = findViewById<Button>(R.id.backButton)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Navigate to DeckFragment using ref: "toCards"
        if (refString == "toCards") {
            val directions = MainFragmentDirections.actionMainFragmentToDeckFragment(string)
            navController.navigate(directions)
            backBtn.visibility = VISIBLE
        }
        // Navigate to CardFragment using ref: "toACard"
        if (refString == "toACard") {
            val directions = DeckFragmentDirections.actionDeckFragmentToCardFragment(string)
            navController.navigate(directions)
            backBtn.visibility = VISIBLE
        }
    }

}
