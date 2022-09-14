package com.example.mobil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DecksAdapter(private val context: Context, private val myList : ArrayList<String>) : RecyclerView.Adapter<DecksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val myDecksItem = LayoutInflater.from(viewGroup.context).inflate(R.layout.deck_item, viewGroup, false)

        return ViewHolder(myDecksItem)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(myList[position])
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val textItem : TextView = itemView.findViewById(R.id.txt)

        fun bind(deckItem : String){
            textItem.text = deckItem
        }
    }
}