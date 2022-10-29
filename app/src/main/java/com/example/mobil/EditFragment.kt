package com.example.mobil

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.adapter.CardsAdapter
import com.example.mobil.adapter.EditAdapter
import com.example.mobil.databinding.FragmentDeckBinding
import com.example.mobil.databinding.FragmentEditBinding
import com.example.mobil.databinding.FragmentMainBinding
import com.example.mobil.model.Card
import com.google.firebase.firestore.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    private var _editBinding: FragmentEditBinding? = null
    private val editBinding get() = _editBinding!!

    private var cards = ArrayList<Card>()
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _editBinding = FragmentEditBinding.inflate(layoutInflater)

        // Adapter & Recycler
        val cardsRecycler = editBinding.cardRecycler
        cardsRecycler.layoutManager = LinearLayoutManager(context)

        val editAdapter = EditAdapter(context = MainActivity(), cards)
        cardsRecycler.adapter = editAdapter

        eventChangeListener(editAdapter)

        //BUTTONS
        val deleteBtn = editBinding.deleteCardBtn
        val ignoreBtn = editBinding.ignoreCardBtn

        // Delete button
        deleteBtn.setOnClickListener {
            val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)
            val selected = editBinding.cardView.findViewById<TextView>(R.id.selectText)

            val deleteDialog = AlertDialog.Builder(context)
            deleteDialog.setView(inflater)

            deleteDialog.setPositiveButton("Delete") {
                    dialog,_->
                for (position in cardsRecycler){
                    if(selected.isVisible){
                        database.collection("Decks").document("t5FymczpG1QDecwshBDw").collection("cards").document("9uGwW8TwVuqDIlPJuyzp").delete()
                        editAdapter.notifyDataSetChanged()
                    }
                }
                dialog.dismiss()
            }

            deleteDialog.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }
            deleteDialog.create()
            deleteDialog.show()
        }

        // Ignore button
        ignoreBtn.setOnClickListener {
            //ToDo: NEEDS FUNCTIONALITY
        }

        // Select card
        editAdapter.setOnCardClickListener(object : EditAdapter.onCardClickListener{
            override fun onCardClick(position: Int) {
                //ToDo: This now needs to work with fragment
                //(activity as DeckActivity).replaceFragment(CardFragment())
                //startActivity(Intent(this@EditFragment.context, CardActivity::class.java))
            }
        })

        return editBinding.root
    }

    private fun eventChangeListener(adapter: EditAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").document("t5FymczpG1QDecwshBDw").collection("cards").
        addSnapshotListener(object : EventListener<QuerySnapshot> {
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
                        cards.add(dc.document.toObject(Card::class.java))
                        Log.e("Add Card Error", cards.toString())
                    }
                }

                adapter.notifyDataSetChanged()
            }
        })
    }

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        // adapter & recycler
        val editAdapter = EditAdapter(context = MainActivity(), cards)
        val editRecycler = view.findViewById<RecyclerView>(R.id.card_recycler)
        editRecycler.adapter = editAdapter
        editRecycler.layoutManager = LinearLayoutManager(context)

        //ToDo: Load deck instead of creating cards here
        cards.add(Card(0, "Card 0 Question", "Card 0 Answer", false))
        cards.add(Card(1, "Card 1 Question", "Card 1 Answer", false))
        cards.add(Card(2, "Card 2 Question", "Card 2 Answer", false))
        cards.add(Card(3, "Card 3 Question", "Card 3 Answer", false))
        cards.add(Card(4, "Card 4 Question", "Card 4 Answer", false))


        // Delete button
        val deleteBtn = view.findViewById<Button>(R.id.deleteCardBtn)
        deleteBtn.setOnClickListener {
            val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)

            val deleteDialog = android.app.AlertDialog.Builder(context)
            deleteDialog.setView(inflater)

            deleteDialog.setPositiveButton("Delete") {
                    dialog,_->
                for (c in cards){
                    if(c.isIgnored == true){
                        cards.remove(c)
                        editAdapter.notifyDataSetChanged()
                    }
                }
                dialog.dismiss()
            }

            deleteDialog.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }
            deleteDialog.create()
            deleteDialog.show()
        }


        // Ignore button
        val ignoreBtn = view.findViewById<Button>(R.id.ignoreCardBtn)
        ignoreBtn.setOnClickListener {

        }

        // Select card
        editAdapter.setOnCardClickListener(object : EditAdapter.onCardClickListener{
            override fun onCardClick(position: Int) {
                //ToDo: This now needs to work with fragment
                //activity as DeckActivity).replaceFragment(CardFragment())
                //startActivity(Intent(this@EditFragment.context, CardActivity::class.java))
            }
        })

        return view
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}