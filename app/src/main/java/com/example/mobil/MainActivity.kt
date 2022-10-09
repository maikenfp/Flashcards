package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.databinding.ActivityMainBinding
import com.example.mobil.model.Card
import com.example.mobil.model.Deck
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        val cardList = ArrayList<Card>()
        val deckList = ArrayList<Deck>()
        deckList.add(Deck(0, "Mobil programmering", cardList))
        deckList.add(Deck(1, "History", cardList))


        val myAdapter = DecksAdapter(this, deckList)
        val myRecycler = mainBinding.myRecyclerView

        val btn = mainBinding.btn

        myRecycler.adapter = myAdapter
        myRecycler.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener{

            val inflater = LayoutInflater.from(this).inflate(R.layout.add_item,null)
            val addtxt = inflater.findViewById<EditText>(R.id.addText)

            val addDialog = AlertDialog.Builder(this)
            addDialog.setView(inflater)

            addDialog.setPositiveButton("Ok"){
                    dialog,_->
                val deckTitle = addtxt.text.toString()
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