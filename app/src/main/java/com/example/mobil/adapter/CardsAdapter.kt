package com.example.mobil.adapter

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.MainActivity
import com.example.mobil.R
import com.example.mobil.model.Card

class CardsAdapter(val context: MainActivity, private val cards: ArrayList<Card>, query : com.google.firebase.firestore.Query) : FirestoreAdapter<CardsAdapter.CardsViewHolder>(query) {

    private lateinit var listener : OnCardClickListener
    private lateinit var longListener : OnLongClickListener

    interface OnCardClickListener{
        fun onCardClick(position: Int)
    }

    fun setOnCardClickListener(listener: OnCardClickListener){
        this.listener = listener
    }

    interface OnLongClickListener{
        fun onLongClick(position: Int)
    }

    fun setOnLongClickListener(longListener: OnLongClickListener){
        this.longListener = longListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): CardsViewHolder {
        val myCardsItem = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return CardsViewHolder(myCardsItem, listener, longListener)
    }

    override fun onBindViewHolder(viewHolder: CardsViewHolder, position: Int) {
        val currentCard = cards[position]
        viewHolder.textItem.text = currentCard.question
        if (currentCard.isIgnored == true) {
            viewHolder.imageItem.visibility = View.VISIBLE
        }
        else{
            viewHolder.imageItem.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    inner class CardsViewHolder (cardView: View, listener: OnCardClickListener, longListener: OnLongClickListener) : RecyclerView.ViewHolder(cardView) {
        val textItem: TextView = cardView.findViewById(R.id.cardTitle)
        val imageItem: ImageView = cardView.findViewById(R.id.cardImage)

        fun bind(cardItem: Card){
            textItem.text = cardItem.toString()
        }

        init {
            cardView.setOnClickListener{
                listener.onCardClick(adapterPosition)
            }
            cardView.setOnLongClickListener {
                longListener.onLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }
    }


}
