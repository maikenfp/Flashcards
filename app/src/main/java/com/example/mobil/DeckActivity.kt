package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.model.Card

class DeckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)

        // Go back button
        //ToDo: Go to previous fragment if in card view
        val goBackBtn = findViewById<Button>(R.id.goBackButton)
        goBackBtn.setOnClickListener{
            startActivity(Intent(this@DeckActivity, MainActivity::class.java))
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.deck_frame_fragment, fragment)
        fragmentTransaction.commit()
    }
}