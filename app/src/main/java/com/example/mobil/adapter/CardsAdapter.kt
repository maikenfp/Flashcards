package com.example.mobil.adapter

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.MainActivity
import com.example.mobil.R
import com.example.mobil.model.Card
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class CardsAdapter(val context: MainActivity, query : com.google.firebase.firestore.Query) : FirestoreAdapter<CardsAdapter.CardsViewHolder>(query) {

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
        getSnapshot(position)?.let { snapshot  -> viewHolder.bind(snapshot) }
    }

    inner class CardsViewHolder (cardView: View, listener: OnCardClickListener, longListener: OnLongClickListener) : RecyclerView.ViewHolder(cardView) {
        val textItem: TextView = cardView.findViewById(R.id.cardTitle)
        val imageItem: ImageView = cardView.findViewById(R.id.cardImage)

        fun bind(snapshot: DocumentSnapshot) {
            val card: Card? = snapshot.toObject(Card::class.java)
            textItem.text = card?.question

            textItem.text = card?.question
            if (card?.isIgnored == true) {
                imageItem.visibility = View.VISIBLE
            } else {
                imageItem.visibility = View.INVISIBLE

            }
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
