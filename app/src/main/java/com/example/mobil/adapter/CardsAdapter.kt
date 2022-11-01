package com.example.mobil.adapter

import android.graphics.drawable.Drawable
import android.view.*
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.DeckActivity
import com.example.mobil.DeckFragment
import com.example.mobil.MainActivity
import com.example.mobil.R
import com.example.mobil.model.Card
import com.google.firebase.firestore.FirebaseFirestore
import java.util.zip.Inflater

class CardsAdapter(val context: MainActivity, private val cards: ArrayList<Card>, val editMenu: () -> Unit) : RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

    private lateinit var listener : OnCardClickListener

    // *****************EDIT*******************
    private val selectedCards = arrayListOf<Card>()
    private var editMode = false
    // *****************EDIT*******************

    interface OnCardClickListener{
        fun onCardClick(position: Int)
    }

    fun setOnCardClickListener(listener: OnCardClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CardsViewHolder {
        val myCardsItem = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return CardsViewHolder(myCardsItem, listener)
    }

    override fun onBindViewHolder(viewHolder: CardsViewHolder, position: Int) {
        val currentCard = cards[position]
        viewHolder.textItem.text = currentCard.question

        // **************************************EDIT**************************************
        val selectText = viewHolder.itemView.findViewById<TextView>(R.id.selectText)

        if (selectedCards.contains(currentCard)) {
            selectText.visibility = View.VISIBLE
        } else {
            selectText.visibility = View.INVISIBLE
        }

        viewHolder.itemView.findViewById<CardView>(R.id.card_item_box).setOnClickListener {
            if (editMode) {
                selectCard(viewHolder, currentCard)
            } else {
                context.navigateToFragment("toACard", "")
            }
        }

        viewHolder.itemView.findViewById<CardView>(R.id.card_item_box).setOnLongClickListener {
            if (!editMode) {
                editMode = true
                editMenu()
                selectCard(viewHolder, currentCard)
            }
            true
        }
        // **************************************EDIT**************************************
    }

    // **************************************EDIT**************************************
    private fun selectCard(holder: CardsViewHolder, card: Card) {
        val selectText = holder.itemView.findViewById<TextView>(R.id.selectText)
        // If the "selectedCards" list contains the card, remove from list and set Invisible
        if (selectedCards.contains(card)) {
            selectedCards.remove(card)
            selectText.visibility = View.INVISIBLE
        } else {
            // else, add the card to "selectedCards" and set visible
            selectedCards.add(card)
            selectText.visibility = View.VISIBLE
        }
    }

    // **************************************EDIT**************************************

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class CardsViewHolder (cardView: View, listener: OnCardClickListener) : RecyclerView.ViewHolder(cardView) {
        val textItem = cardView.findViewById<TextView>(R.id.cardTitle)

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
