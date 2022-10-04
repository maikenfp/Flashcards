package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.example.mobil.model.Card

class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        var index = 0
        val cardText: TextView = findViewById(R.id.cardTextView)

        // Current Deck
        // ToDo: This ArrayList should be loaded with the correct deckId
        val cards = ArrayList<Card>()
        cards.add(Card(0, "Card 0 Question", "Card 0 Answer", 0, false))
        cards.add(Card(1, "Card 1 Question", "Card 1 Answer", 0, false))
        cards.add(Card(2, "Card 2 Question", "Card 2 Answer", 0, false))
        cards.add(Card(3, "Card 3 Question", "Card 3 Answer", 0, false))
        cards.add(Card(4, "Card 4 Question", "Card 4 Answer", 0, false))


        // Go back button
        val goBackBtn = findViewById<Button>(R.id.goBackButton)
        goBackBtn.setOnClickListener{
            startActivity(Intent(this@CardActivity, DeckActivity::class.java))
        }


        // Hamburger Menu button
        // ToDo: Make Hamburger Button correctly
        val editBtn = findViewById<Button>(R.id.editModeButton)
        editBtn.setOnClickListener{
            val inflater = LayoutInflater.from(this).inflate(R.layout.add_card,null)
        }

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