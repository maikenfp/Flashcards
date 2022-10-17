package com.example.mobil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.R
import com.example.mobil.model.Deck

class DecksAdapter(val context: Context, private val myList : ArrayList<Deck>) : RecyclerView.Adapter<DecksAdapter.ViewHolder>() {

    lateinit var listener : OnItemClickListener

    interface OnItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){

        this.listener = listener

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val myDecksItem = LayoutInflater.from(viewGroup.context).inflate(R.layout.deck_item, viewGroup, false)

        return ViewHolder(myDecksItem, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val newList : Deck = myList[position]

        viewHolder.textItem.text = newList.title
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    inner class ViewHolder(itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val textItem : TextView = itemView.findViewById(R.id.deckTitle)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }


}