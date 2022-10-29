package com.example.mobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.firestore.*


class MainActivity : AppCompatActivity() {
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Go back button
        val goBackBtn = findViewById<ImageView>(R.id.goBackButton)
        goBackBtn.setOnClickListener{
            this.replaceFragment(MainFragment())
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.main_fragment_container, fragment)
        fragmentTransaction.commit()
    }


}
