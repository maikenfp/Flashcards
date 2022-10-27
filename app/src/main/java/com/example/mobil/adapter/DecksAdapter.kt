package com.example.mobil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.MainActivity
import com.example.mobil.MainFragment
import com.example.mobil.R
import com.example.mobil.model.Deck
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore

class DecksAdapter(val context: MainActivity, private val deckList : ArrayList<Deck>) : RecyclerView.Adapter<DecksAdapter.ViewHolder>() {

    private lateinit var listener : OnItemClickListener

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
        val newList : Deck = deckList[position]

        viewHolder.textItem.text = newList.title
    }

    override fun getItemCount(): Int {
        return deckList.size
    }

    inner class ViewHolder(itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var menu : ImageView = itemView.findViewById(R.id.hamburger_menu)
        val textItem : TextView = itemView.findViewById(R.id.deckTitle)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
            menu.setOnClickListener{ popupMenu(menu) }
        }

        private fun popupMenu(view : View) {
            val db = FirebaseFirestore.getInstance()
            val position = deckList[adapterPosition]
            val popupMenu = PopupMenu(context, view)
            popupMenu.inflate(R.menu.show_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editDeckName->{
                        val editView = LayoutInflater.from(context).inflate(R.layout.add_deck, null)
                        val deckName = editView.findViewById<TextView>(R.id.addDeckName)

                        val addDialog = AlertDialog.Builder(context)
                        addDialog.setView(editView)

                        addDialog.setPositiveButton("Ok"){
                                dialog,_->
                            position.title = deckName.text.toString()
                            //Må finne ut hvordan koble til riktig dokument onclick
                            db.collection("Decks").document().update("title", position.title)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }
                        addDialog.setNegativeButton("Cancel"){
                                dialog,_->
                            dialog.dismiss()
                        }
                            .create()
                            .show()
                        true
                    }
                    R.id.deleteDeck->{
                        AlertDialog.Builder(context).setTitle("Delete").setIcon(R.drawable.ic_warning).setMessage("Are you sure you want to delete this deck?")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                //Må finne ut hvordan koble til riktig dokument onclick
                                db.collection("Decks").document("Test").delete()
                                notifyDataSetChanged()
                                dialog.dismiss()
                            }
                        .setNegativeButton("Cancel"){
                                dialog,_->
                            dialog.dismiss()
                        }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }

            popupMenu.show()
        }
    }
}