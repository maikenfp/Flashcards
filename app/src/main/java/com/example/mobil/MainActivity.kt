package com.example.mobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.mobil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

    }

    fun navigateToFragment(refString: String, string: String) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // Navigate to DeckFragment using ref: "toCards"
        if (refString == "toCards") {
            val directions = MainFragmentDirections.actionMainFragmentToDeckFragment(string)
            navController.navigate(directions)
        }
        // Navigate to CardFragment using ref: "toACard"
        if (refString == "toACard") {
            val directions = DeckFragmentDirections.actionDeckFragmentToCardFragment(string)
            navController.navigate(directions)
        }
        // Navigate to EditFragment using ref: "toEdit"
        if (refString == "toEdit") {
            val directions = DeckFragmentDirections.actionDeckFragmentToEditFragment()
            navController.navigate(directions)
        }
    }
}
