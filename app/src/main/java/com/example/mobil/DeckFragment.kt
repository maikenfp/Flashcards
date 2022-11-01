package com.example.mobil

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobil.adapter.CardsAdapter
import com.example.mobil.databinding.ActivityTestBinding.inflate
import com.example.mobil.databinding.DeckItemBinding.inflate
import com.example.mobil.databinding.FragmentDeckBinding
import com.example.mobil.model.Card
import com.google.firebase.firestore.*
import kotlin.random.Random

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
        _deckBinding = FragmentDeckBinding.inflate(layoutInflater)
        return deckBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter & Recycler
        val cardsRecycler = deckBinding.cardRecycler
        cardsRecycler.layoutManager = LinearLayoutManager(context)

        val cardsAdapter = CardsAdapter(context = MainActivity(), cards, ::editMenu)
        cardsRecycler.adapter = cardsAdapter

        eventChangeListener(cardsAdapter)

        //BUTTONS
        val addCardBtn = deckBinding.addCardBtn
        val shuffleBtn = deckBinding.shuffleBtn

        // ADD CARD
        addCardBtn.setOnClickListener {
            val inflater = LayoutInflater.from(context).inflate(R.layout.add_card, null)
            val addQuestion = inflater.findViewById<EditText>(R.id.enter_question)
            val addAnswer = inflater.findViewById<EditText>(R.id.enter_answer)

            val addCardDialog = AlertDialog.Builder(context)
            addCardDialog.setView(inflater)

            addCardDialog.setPositiveButton("Save") {
                    dialog,_->
                val question = addQuestion.text.toString()
                val answer = addAnswer.text.toString()
                val card = hashMapOf(
                    "question" to question,
                    "answer" to answer,
                    "isIgnored" to false
                )
                database.collection("Decks")
                    .document(args.docId.toString())
                    .collection("cards")
                    .add(card)

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

        // Shuffle button
        shuffleBtn.setOnClickListener {
            //ToDo: NEEDS FUNCTIONALITY
            val shuffleIndex = Random.nextInt(cards.size)
            val shuffleElement = cards[shuffleIndex]

        }

        // Go to card (might be useless)
        cardsAdapter.setOnCardClickListener(object : CardsAdapter.OnCardClickListener{
            override fun onCardClick(position: Int) {
                val currentId = database.collection("Decks").document().collection("cards").document(cards[position].docId.toString()).id
                (activity as MainActivity).navigateToFragment("toACard", currentId)

                Log.e("NAVIGATE TO CARD ID: ", currentId)
            }
        })
    }

    fun editMenu() {
        val buttonBox = view?.findViewById<CardView>(R.id.cardView)
        if (buttonBox != null) {
            buttonBox.visibility = GONE
        }
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.delete_cards -> {
                        //TODO()
                        return true
                    }
                    R.id.ignore_cards -> {
                        //TODO()
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun eventChangeListener(adapter: CardsAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").document(args.docId.toString()).collection("cards").
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
                        Log.e("Load Decks LOG", cards.toString())
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
