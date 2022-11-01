package com.example.mobil

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.adapter.DecksAdapter
import com.example.mobil.model.Deck
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private var decks = ArrayList<Deck>()
    private lateinit var database : FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query : Query = FirebaseFirestore.getInstance().collection("Decks")

        // adapter & recycler
        val decksAdapter = DecksAdapter(context = MainActivity(), decks, query)
        val decksRecycler = view.findViewById<RecyclerView>(R.id.deck_recycler)
        decksRecycler.adapter = decksAdapter
        decksRecycler.layoutManager = LinearLayoutManager(context)

        eventChangeListener(decksAdapter)

        //Add Deck
        val addDeckBtn = view.findViewById<Button>(R.id.addDeckButton)
        addDeckBtn.setOnClickListener {
            val inflater = LayoutInflater.from(context).inflate(R.layout.add_deck, null)
            val addTxt = inflater.findViewById<EditText>(R.id.addDeckName)

            val addDeckDialog = AlertDialog.Builder(context)
            addDeckDialog.setView(inflater)

            addDeckDialog.setPositiveButton("Ok") {
                    dialog,_->
                val deckName = addTxt.text.toString()
                val deck = hashMapOf(
                    "title" to deckName
                    //"cards" to cardList
                )

                database.collection("Decks").add(deck)
                decksAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            addDeckDialog.setNegativeButton("Cancel") {
                    dialog,_->
                dialog.dismiss()
            }
            addDeckDialog.create()
            addDeckDialog.show()
        }

        decksAdapter.setOnItemClickListener(object : DecksAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                //(activity as MainActivity).replaceFragment(DeckFragment())
                val currentId = database.collection("Decks").document(decks[position].docId.toString()).id
                (activity as MainActivity).navigateToFragment("toCards", currentId)

                Log.e("NAVIGATE TO DECK ID: ", currentId)
            }
        })
    }

    private fun eventChangeListener(adapter: DecksAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").
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
                        decks.add(dc.document.toObject(Deck::class.java))
                    }
                }

                adapter.notifyDataSetChanged()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}