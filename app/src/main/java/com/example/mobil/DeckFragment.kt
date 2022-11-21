package com.example.mobil

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobil.adapter.CardsAdapter
import com.example.mobil.databinding.FragmentDeckBinding
import com.example.mobil.model.Card
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DeckFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeckFragment : Fragment() {

    //testing ********************
    private val args: DeckFragmentArgs by navArgs()
    //testing ********************

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


    val query : Query = database.collection("Decks").whereEqualTo("userID", firebaseAuth.currentUser?.uid)
    val cardsAdapter = CardsAdapter(context = MainActivity(), cards, query)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _deckBinding = FragmentDeckBinding.inflate(layoutInflater)
        return deckBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    override fun onPause() {
        super.onPause()
        cardsAdapter.stopListening()
        cards = ArrayList<Card>()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DeckFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeckFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
