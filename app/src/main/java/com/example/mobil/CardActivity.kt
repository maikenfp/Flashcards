package com.example.mobil


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mobil.model.Card


class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)


        // Go back button
        val goBackBtn = findViewById<Button>(R.id.goBackButton)
        goBackBtn.setOnClickListener{
            startActivity(Intent(this@CardActivity, DeckActivity::class.java))
        }


        // Hamburger Menu button
        // ToDo: Make Hamburger Button correctly
        //val hamburgerBtn = findViewById<Button>(R.id.editModeButton)
        //hamburgerBtn.setOnClickListener{
        //}


    }
}