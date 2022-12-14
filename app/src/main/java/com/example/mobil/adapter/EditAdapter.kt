package com.example.mobil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.MainActivity
import com.example.mobil.R
import com.example.mobil.model.Card

class EditAdapter(val context: MainActivity, private val cards: ArrayList<Card>) : RecyclerView.Adapter<EditAdapter.CardsViewHolder>() {

    val selectedCards = arrayListOf<Card>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CardsViewHolder {
        val myCardsItem = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return CardsViewHolder(myCardsItem)
    }

    override fun onBindViewHolder(viewHolder: CardsViewHolder, position: Int) {
        val selectText = viewHolder.itemView.findViewById<TextView>(R.id.selectText)

        // Get the current card
        val currentCard = cards[position]
        viewHolder.textItem.text = currentCard.question

        // for every card, check to see if it exists in the array
        if (selectedCards.contains(currentCard)) {
            // if the card is selected, let the user know by making text visible
            selectText.visibility = View.VISIBLE
        } else {
            // else, keep it as it is
            selectText.visibility = View.INVISIBLE
        }
        if (currentCard.isIgnored == true) {
            viewHolder.imageItem.visibility = View.VISIBLE
        }
        else{
            viewHolder.imageItem.visibility = View.INVISIBLE
        }

        viewHolder.itemView.findViewById<CardView>(R.id.card_item_box).setOnClickListener {
            // if the user is in multi-select mode, add it to the selected cards list
            selectCard(viewHolder, currentCard)
        }
    }

    // helper function that adds/removes a card to the array depending on the app's state
    private fun selectCard(holder: CardsViewHolder, card: Card) {
        val selectText = holder.itemView.findViewById<TextView>(R.id.selectText)
        // If the "selectedCards" array contains the card, remove from array and set Invisible
        if (selectedCards.contains(card)) {
            selectedCards.remove(card)
            selectText.visibility = View.INVISIBLE
        } else {
            // else, add the card to "selectedCards" and set visible
            selectedCards.add(card)
            selectText.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class CardsViewHolder (cardView: View) : RecyclerView.ViewHolder(cardView) {

        val textItem: TextView = cardView.findViewById(R.id.cardTitle)
        val imageItem: ImageView = cardView.findViewById(R.id.cardImage)
    }

}
