package com.example.mobil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    var myList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myList.add("Notebook")

        val myAdapter = DecksAdapter(this, myList)
        val myRecycler = findViewById<RecyclerView>(R.id.my_recycler_view)
        val btn = findViewById<Button>(R.id.btn)
        //val txt = findViewById<TextView>(R.id.addDeck)

        //btn.setOnClickListener{
         //   myList.add(txt.text.toString())
            //myAdapter.notifyDataSetChanged()
        //}

        myRecycler.adapter = myAdapter
        myRecycler.layoutManager = LinearLayoutManager(this)

    }
}