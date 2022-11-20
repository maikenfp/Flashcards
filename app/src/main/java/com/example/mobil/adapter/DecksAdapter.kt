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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DecksAdapter(val context: MainActivity, query : com.google.firebase.firestore.Query) : FirestoreAdapter<DecksAdapter.ViewHolder>(query) {

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
        getSnapshot(position)?.let { snapshot -> viewHolder.bind(snapshot) }
    }

    inner class ViewHolder(itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private var menu : ImageView = itemView.findViewById(R.id.hamburger_menu)
        private val deckTitle : TextView = itemView.findViewById(R.id.deckTitle)

        fun bind(snapshot: DocumentSnapshot) {
            val deck: Deck? = snapshot.toObject(Deck::class.java)
            deckTitle.text = deck?.title
        }

        init {
            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
            menu.setOnClickListener{ popupMenu(menu) }
        }

        private fun popupMenu(view : View) {
            val db = FirebaseFirestore.getInstance()
            val position = getSnapshot(adapterPosition)
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
                            val addedDeckName = deckName.text.toString()
                            if (position != null) {
                                db.collection("Decks").document(position.id).update("title", addedDeckName)
                            }
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
                        AlertDialog.Builder(view.context).setTitle("Delete").setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure you want to delete this deck? This can not be undone.")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                if (position != null) {
                                    db.collection("Decks").document(position.id).delete()
                                }
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
