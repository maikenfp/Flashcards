package com.example.mobil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.MainActivity
import com.example.mobil.R
import com.example.mobil.model.Card

class EditAdapter(val context: MainActivity, private val cards: ArrayList<Card>) : RecyclerView.Adapter<EditAdapter.CardsViewHolder>() {

    private val selectedCards = arrayListOf<Card>()

    lateinit var listener : onCardClickListener

    interface onCardClickListener{
        fun onCardClick(position: Int)
    }

    fun setOnCardClickListener(listener: onCardClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CardsViewHolder {
        val myCardsItem = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return CardsViewHolder(myCardsItem, listener)
    }

    override fun onBindViewHolder(viewHolder: CardsViewHolder, position: Int) {
        val selectText = viewHolder.itemView.findViewById<TextView>(R.id.selectText)

        // Get the current card
        val currentCard = cards[position]
        viewHolder.bind(currentCard)
        // for every card, check to see if it exists in the array
        if (selectedCards.contains(currentCard)) {
            // if the card is selected, let the user know by making text visible
            selectText.visibility = View.VISIBLE
        } else {
            // else, keep it as it is
            selectText.visibility = View.INVISIBLE
        }

        // handler to define what happens when an item is tapped
        viewHolder.itemView.findViewById<CardView>(R.id.card_item_box).setOnClickListener {
            // if the user is in multi-select mode, add it to the multi select list
            selectCard(viewHolder, currentCard)
        }
    }

    // helper function that adds/removes an item to the list depending on the app's state
    private fun selectCard(holder: CardsViewHolder, card: Card) {
        val selectText = holder.itemView.findViewById<TextView>(R.id.selectText)
        // If the "selectedItems" list contains the item, remove it and set it's state to normal
        if (selectedCards.contains(card)) {
            selectedCards.remove(card)
            selectText.visibility = View.INVISIBLE
        } else {
            // Else, add it to the list and add a darker shade over the image, letting the user know that it was selected
            selectedCards.add(card)
            selectText.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class CardsViewHolder (cardView: View, listener: onCardClickListener) : RecyclerView.ViewHolder(cardView) {

        private val textItem = cardView.findViewById<TextView>(R.id.cardTitle)

        fun bind(cardItem: Card){
            textItem.text = cardItem.toString()
        }

        init {
            cardView.setOnClickListener{
                listener.onCardClick(adapterPosition)
            }
        }

    }

}
