package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.model.Card

class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)

        val cards = ArrayList<Card>()
        cards.add(Card(0, "What course is this?", "Android Programming!", 0, false))

        // adapter & recycler
        val cardAdapter = CardsAdapter(this, cards)
        val cardRecycler = findViewById<RecyclerView>(R.id.card_recycle_view)

        cardRecycler.adapter = cardAdapter
        cardRecycler.layoutManager = LinearLayoutManager(this)

        // Add button
        val addBtn = findViewById<Button>(R.id.addCardButton)

        addBtn.setOnClickListener {
            val inflater = LayoutInflater.from(this).inflate(R.layout.add_card, null)
            val addQuestion = inflater.findViewById<EditText>(R.id.input_question)
            val addAnswer = inflater.findViewById<EditText>(R.id.input_answer)

            val addCardDialog = AlertDialog.Builder(this)
            addCardDialog.setView(inflater)

            addCardDialog.setPositiveButton("Save") {
                    dialog,_->
                val question = addQuestion.text.toString()
                val answer = addAnswer.text.toString()
                cards.add(Card(1, question, answer, 1, false))
                cardAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            addCardDialog.setNegativeButton("Cancel") {
                    dialog,_->
                dialog.dismiss()
            }
        }

        // Edit button
        val editBtn = findViewById<Button>(R.id.editModeButton)
        editBtn.setOnClickListener{
            val inflater = LayoutInflater.from(this).inflate(R.layout.add_card,null)
            val addtxt = inflater.findViewById<EditText>(R.id.addText)
        }

        // Shuffle button
        val shuffleBtn = findViewById<Button>(R.id.shuffleButton)
        shuffleBtn.setOnClickListener{
            val inflater = LayoutInflater.from(this).inflate(R.layout.add_card,null)
            val addtxt = inflater.findViewById<EditText>(R.id.addText)
        }

        // Go back button
        val goBackBtn = findViewById<Button>(R.id.goBackButton)
        goBackBtn.setOnClickListener{
            startActivity(Intent(this@CardActivity, DeckActivity::class.java))
        }

        // Go to card
        cardAdapter.setOnCardClickListener(object : CardsAdapter.onCardClickListener{
            override fun onCardClick(position: Int) {
                startActivity(Intent(this@CardActivity, Test::class.java))
            }
        })

        // Go to card
        cardAdapter.setOnCardClickListener(object : CardsAdapter.onCardClickListener{
            override fun onCardClick(position: Int) {
                
            }
        })
    }
}