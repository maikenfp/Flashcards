package com.example.mobil

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobil.adapter.DecksAdapter
import com.example.mobil.databinding.ActivityMainBinding
import com.example.mobil.model.Card
import com.example.mobil.model.Deck
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    //private lateinit var database : DatabaseReference
    private lateinit var database : FirebaseFirestore
    private lateinit var deck: ArrayList<Deck>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        //Deck list hardcoded dummy data
        val cardList = ArrayList<Card>()
        /*val deckList = ArrayList<Deck>()
        cardList.add(Card(0, "Card 0 Question", "Card 0 Answer", 0, false))
        deckList.add(Deck(0, "Mobil programmering"))
        deckList.add(Deck(1, "History"))*/

        val myRecycler = mainBinding.myRecyclerView
        myRecycler.layoutManager = LinearLayoutManager(this)

        deck = arrayListOf()

        val myAdapter = DecksAdapter(this, deck)
        myRecycler.adapter = myAdapter

        eventChangeListener(myAdapter)

        val addDeckButton = mainBinding.addDeckButton

        val testbtn = mainBinding.test

        //DATABASE IMPLEMEMTATION TESTING
        testbtn.setOnClickListener{

            //Cloud Firestore TEST
            val user = hashMapOf(
                "first" to "Ada",
                "last" to "Lovelace",
                "born" to 1815
            )

            database.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            //Realtime DB
            /*val decktest = Deck(4, "Test 4", cardList)

            val id = database.push().key!!
            //val myRef = db.getReference("message")
            database.child(id).setValue(decktest)

            //myRef.setValue("Hello, World!")*/
        }

        //On "Add deck" Click
        addDeckButton.setOnClickListener{

            val inflater = LayoutInflater.from(this).inflate(R.layout.add_deck,null)
            val addText = inflater.findViewById<EditText>(R.id.addText)

            val addDialog = AlertDialog.Builder(this)
            addDialog.setView(inflater)

            addDialog.setPositiveButton("Ok"){
                    dialog,_->
                val deckTitle = addText.text.toString()

                val deck = hashMapOf(
                    "id" to 0,
                    "title" to deckTitle,
                    "cards" to cardList
                )

                database.collection("Decks").add(deck)
                myAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            addDialog.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }

            addDialog.create()
            addDialog.show()

        }

        myAdapter.setOnItemClickListener(object : DecksAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                startActivity(Intent(this@MainActivity, DeckActivity::class.java))
            }

        })

    }

    private fun eventChangeListener(adapter: DecksAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").
                addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(error != null){
                            Log.e("Firestore Error", error.message.toString())
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            if(dc.type == DocumentChange.Type.ADDED){
                                deck.add(dc.document.toObject(Deck::class.java))
                            }
                        }

                        adapter.notifyDataSetChanged()
                    }

                })
    }
}