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

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val myList = ArrayList<String>()
        myList.add("Mobile Programming")
        myList.add("History")

        val myAdapter = DecksAdapter(this, myList)
        val myRecycler = findViewById<RecyclerView>(R.id.my_recycler_view)

        val btn = findViewById<Button>(R.id.btn)

        myRecycler.adapter = myAdapter
        myRecycler.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener{

            val inflater = LayoutInflater.from(this).inflate(R.layout.add_item,null)
            val addtxt = inflater.findViewById<EditText>(R.id.addText)

            val addDialog = AlertDialog.Builder(this)
            addDialog.setView(inflater)

            addDialog.setPositiveButton("Ok"){
                    dialog,_->
                val txt = addtxt.text.toString()
                myList.add(txt)
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
                startActivity(Intent(this@MainActivity, Test::class.java))
            }

        })

    }
}