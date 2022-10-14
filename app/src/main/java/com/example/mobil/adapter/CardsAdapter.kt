package com.example.mobil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.DeckActivity
import com.example.mobil.R
import com.example.mobil.model.Card

class CardsAdapter(val context: DeckActivity, private val cards: List<Card>) : RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

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
        val currentCard = cards[position]
        viewHolder.bind(currentCard)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class CardsViewHolder (cardView: View, listener: onCardClickListener) : RecyclerView.ViewHolder(cardView) {

        private val textItem = cardView.findViewById<TextView>(R.id.testText)

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
