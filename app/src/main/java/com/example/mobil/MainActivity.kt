package com.example.mobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import androidx.fragment.app.Fragment
import com.example.mobil.adapter.CardsAdapter
import com.example.mobil.adapter.DecksAdapter
import com.example.mobil.model.Deck
import com.google.firebase.firestore.*


class MainActivity : AppCompatActivity() {

    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.deck_frame_fragment, fragment)
        fragmentTransaction.commit()
    }

}
