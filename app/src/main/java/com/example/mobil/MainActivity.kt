package com.example.mobil

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobil.databinding.ActivityMainBinding
import com.example.mobil.databinding.AddItemBinding
import com.example.mobil.model.Card
import com.example.mobil.model.Deck
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        val db = Firebase.firestore
        database = FirebaseDatabase.getInstance().getReference("Decks")


        val cardList = ArrayList<Card>()
        val deckList = ArrayList<Deck>()
        cardList.add(Card(0, "Card 0 Question", "Card 0 Answer", 0, false))
        deckList.add(Deck(0, "Mobil programmering", cardList))
        deckList.add(Deck(1, "History", cardList))

        val myAdapter = DecksAdapter(this, deckList)
        val myRecycler = mainBinding.myRecyclerView

        val btn = mainBinding.btn

        val testbtn = mainBinding.test

        //DATABASE IMPLEMEMTATION TESTING
        testbtn.setOnClickListener{

            //Cloud Firestore TEST
            val user = hashMapOf(
                "first" to "Ada",
                "last" to "Lovelace",
                "born" to 1815
            )

            db.collection("users")
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

        myRecycler.adapter = myAdapter
        myRecycler.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener{

            val inflater = LayoutInflater.from(this).inflate(R.layout.add_item,null)
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

                db.collection("Decks").add(deck)

                deckList.add(Deck(2, deckTitle, cardList))
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

        myAdapter.setOnItemClickListener(object : DecksAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                startActivity(Intent(this@MainActivity, DeckActivity::class.java))
            }

        })

    }
}