package com.example.mobil

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.adapter.DecksAdapter
import com.example.mobil.model.Deck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestoreSettings
import java.lang.Error

class MyDecksFragment : Fragment() {

    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private var decks = ArrayList<Deck>()
    private var database : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val query : Query = database.collection("Decks").whereEqualTo("userID", firebaseAuth.currentUser?.uid)
    private val decksAdapter = DecksAdapter(context = MainActivity(), query)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Able to store data locally offline
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }

        database.firestoreSettings = settings

        val decksRecycler = view.findViewById<RecyclerView>(R.id.deck_recycler)
        decksRecycler.adapter = decksAdapter
        decksRecycler.layoutManager = LinearLayoutManager(context)

        eventChangeListener(decksAdapter)

        //Log out
        val logout = view.findViewById<TextView>(R.id.logOut)
        logout.setOnClickListener{
            try {
                firebaseAuth.signOut()
                activity.let {
                    val intent = Intent(it, MainActivity::class.java)
                    startActivity(intent)
                }
            } catch (error : Error){
                Log.e("Error in sign out", error.message.toString())
            }
        }

        //Add Deck
        val addDeckBtn = view.findViewById<Button>(R.id.addDeckButton)
        addDeckBtn.setOnClickListener {
            addDeck()

        }

        decksAdapter.setOnItemClickListener(object : DecksAdapter.OnItemClickListener {
            override fun onItemClick(position: DocumentSnapshot?) {
                val currentTitle = position?.get("title")
                val currentId = position?.id?.let { database.collection("Decks").document(it).id }
                val directions = MyDecksFragmentDirections.actionMainFragmentToDeckFragment(currentId,
                    currentTitle as String??)
                findNavController().navigate(directions)
            }
        })
    }

    private fun addDeck() {
        val inflater = LayoutInflater.from(context).inflate(R.layout.add_deck, null)
        val addTxt = inflater.findViewById<EditText>(R.id.addDeckName)

        val addDeckDialog = AlertDialog.Builder(context)
        addDeckDialog.setView(inflater)

        addDeckDialog.setPositiveButton("Ok") {
                dialog,_->
            val deckID = System.currentTimeMillis().toString()
            val deckName = addTxt.text.toString()
            val userID = firebaseAuth.currentUser?.uid
            val deck = hashMapOf(
                "userID" to userID,
                "title" to deckName
            )
            database.collection("Decks").document(deckID).set(deck)
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

    private fun eventChangeListener(adapter: DecksAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").whereEqualTo("userID", firebaseAuth.currentUser?.uid).
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

    override fun onStart() {
        super.onStart()
        decksAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        decksAdapter.stopListening()
    }
}