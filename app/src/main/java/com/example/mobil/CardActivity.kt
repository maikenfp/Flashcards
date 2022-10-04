package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mobil.model.Card

class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        var index = 0
        val cardText = findViewById<TextView>(R.id.cardTextView)

        // Current Deck
        // ToDo: This ArrayList should be loaded with the correct deckId
        val cards = ArrayList<Card>()

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

        // Previous Card button
        val previousCardBtn = findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener{
            index -= 1
            cardText.text = cards[index].question
        }


        // Next Card Button
        val nextCardBtn = findViewById<Button>(R.id.nextCardButton)
        nextCardBtn.setOnClickListener{
            index += 1
            cardText.text = cards[index].question
        }

        // Flip Card Button
        val flipCardBtn = findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener{
            if (cardText.text == cards[index].question) {
                cardText.text = cards[index].answer
            }
            else {
                cardText.text = cards[index].question
            }
        }
    }
}