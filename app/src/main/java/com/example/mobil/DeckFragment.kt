package com.example.mobil

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobil.adapter.CardsAdapter
import com.example.mobil.databinding.FragmentDeckBinding
import com.example.mobil.model.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class DeckFragment : Fragment() {

    private val args: DeckFragmentArgs by navArgs()

    private var _deckBinding: FragmentDeckBinding? = null
    private val deckBinding get() = _deckBinding!!

    private var cards = ArrayList<Card>()
    private var database : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var shuffle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        Log.e("HELLO", args.deckId.toString())
    }

    private val query : Query = database.collection("Decks").whereEqualTo("userID", firebaseAuth.currentUser?.uid)
    private var cardsAdapter = CardsAdapter(context = MainActivity(), cards, query)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _deckBinding = FragmentDeckBinding.inflate(layoutInflater)
        return deckBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shuffle = false

        // Adapter & Recycler
        val cardsRecycler = deckBinding.cardRecycler
        cardsRecycler.layoutManager = LinearLayoutManager(context)
        cardsRecycler.adapter = cardsAdapter

        eventChangeListener(cardsAdapter)

        //BUTTONS
        val addCardBtn = deckBinding.addCardBtn
        val shuffleBtn = deckBinding.shuffleBtn
        val editBtn = deckBinding.editModeBtn

        // ADD CARD
        addCardBtn.setOnClickListener {
            addCard()
        }

        // Shuffle button
        shuffleBtn.setOnClickListener {
            shuffle = !shuffle
            setIconTint()
        }

        //Go to Edit by edit button
        editBtn.setOnClickListener {
            val currentDeckId = database.collection("Decks").document(args.deckId.toString()).id
            val currentTitle = args.deckTitle.toString()
            val directions = DeckFragmentDirections.actionDeckFragmentToEditFragment(currentDeckId, currentTitle)
            findNavController().navigate(directions)
        }

        //Go to Edit by longclick
        cardsAdapter.setOnLongClickListener(object : CardsAdapter.OnLongClickListener{
            override fun onLongClick(position: Int) {
                val currentDeckId = database.collection("Decks").document(args.deckId.toString()).id
                val currentTitle = args.deckTitle.toString()
                val directions = DeckFragmentDirections.actionDeckFragmentToEditFragment(currentDeckId, currentTitle)
                findNavController().navigate(directions)
            }
        })

        // Go to card
        cardsAdapter.setOnCardClickListener(object : CardsAdapter.OnCardClickListener{
            override fun onCardClick(position: Int) {
                val currentDeckId = database.collection("Decks").document(args.deckId.toString()).id
                val currentCardId = database.collection("Decks").document(args.deckId.toString()).collection("cards").document(cards[position].docId.toString()).id
                val currentDeckTitle = args.deckTitle.toString()
                val directions = DeckFragmentDirections.actionDeckFragmentToCardFragment(currentDeckId, currentCardId, currentDeckTitle, shuffle)
                findNavController().navigate(directions)

                Log.e("NAVIGATE TO CARD ID: ", currentCardId)
            }
        })
    }

    private fun setIconTint() {
        val btn = deckBinding.shuffleBtn
        if (shuffle) {
            TextViewCompat.setCompoundDrawableTintList(btn, ColorStateList.valueOf(context.let { ContextCompat.getColor(requireContext(), R.color.shuffleActive) }) )
        } else if (!shuffle) {
            TextViewCompat.setCompoundDrawableTintList(btn, ColorStateList.valueOf(context.let { ContextCompat.getColor(requireContext(), R.color.md_theme_light_background) }) )
        }
    }

    private fun addCard() {
        val inflater = LayoutInflater.from(context).inflate(R.layout.add_card, null)
        val addQuestion = inflater.findViewById<EditText>(R.id.enter_question)
        val addAnswer = inflater.findViewById<EditText>(R.id.enter_answer)

        val addCardDialog = AlertDialog.Builder(context)
        addCardDialog.setView(inflater)

        addCardDialog.setPositiveButton("Save") {
                dialog,_->
            val cardID = System.currentTimeMillis().toString()
            val question = addQuestion.text.toString()
            val answer = addAnswer.text.toString()
            val card = hashMapOf(
                "question" to question,
                "answer" to answer,
                "isIgnored" to false
            )
            database.collection("Decks")
                .document(args.deckId.toString())
                .collection("cards")
                .document(cardID)
                .set(card)

            cardsAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        addCardDialog.setNegativeButton("Cancel") {
                dialog,_->
            dialog.dismiss()
        }
        addCardDialog.create()
        addCardDialog.show()
    }

    private fun eventChangeListener(adapter: CardsAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").document(args.deckId.toString()).collection("cards").
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
                        Log.e("Load Decks TEST", cards.toString())
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        cardsAdapter.startListening()
        cardsAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        cardsAdapter.stopListening()
        cards = ArrayList<Card>()
        cardsAdapter = CardsAdapter(context = MainActivity(), cards, query)
    }
}
