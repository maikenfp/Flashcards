package com.example.mobil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.*
import com.example.mobil.model.Deck
import com.google.firebase.firestore.FirebaseFirestore

class DecksAdapter(val context: MainActivity, private val decks : ArrayList<Deck>, query : com.google.firebase.firestore.Query) : FirestoreAdapter<DecksAdapter.ViewHolder>(query) {

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
        val currentDeck = decks[position]
        viewHolder.textItem.text = currentDeck.title
    }

    override fun getItemCount(): Int {
        return decks.size
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
            val position = decks[adapterPosition]
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.show_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editDeckName->{
                        val editView = LayoutInflater.from(view.context).inflate(R.layout.add_deck, null)
                        val deckName = editView.findViewById<TextView>(R.id.addDeckName)

                        val addDialog = AlertDialog.Builder(view.context)
                        addDialog.setView(editView)

                        addDialog.setPositiveButton("Ok"){
                                dialog,_->
                            position.title = deckName.text.toString()
                            db.collection("Decks").document(position.docId.toString()).update("title", position.title)
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
                        AlertDialog.Builder(view.context).setTitle("Delete").setIcon(R.drawable.ic_warning).setMessage("Are you sure you want to delete this deck?")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                //Må finne ut hvordan koble til riktig dokument onclick
                                db.collection("Decks").document(position.docId.toString()).delete()
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
