package com.example.mobil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.mobil.model.Card
import com.google.firebase.firestore.*

class CardFragment : Fragment() {
    private val argsCard: CardFragmentArgs by navArgs()
    private var cards = ArrayList<Card>()
    private lateinit var database : FirebaseFirestore
    private val cardText = view?.findViewById<TextView>(R.id.cardTextView)
    var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //ToDo: load deck and choose starting index
            //int = it.getString(ARG_PARAM1)
            //cards = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        index = 0

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Loads non-ignored cards into a local navigatable ArrayList
        loadDeck()

        // Previous Card button
        val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener{
            if (index == 0) {
                index = cards.size -1
            }
            else {
                index -= 1
            }
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question

        }

        // Next Card Button
        val nextCardBtn = view.findViewById<Button>(R.id.nextCardButton)
        nextCardBtn.setOnClickListener{
            if (index == cards.size -1) {
                index = 0
            }
            else {
                index += 1
            }
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
        }

        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener{
            if (view?.findViewById<TextView>(R.id.cardTextView)?.text == cards[index].question) {
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].answer
            }
            else {
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
            }
        }
    }

    //Loads deck from firestore
    private fun loadDeck() {
        database = FirebaseFirestore.getInstance()
        Log.e("Load Decks LOG", database.toString())
        var cardIndex = 0

        database.collection("Decks").document(argsCard.deckId.toString()).collection("cards").get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")

                    if (document.id == argsCard.cardId) {
                        index = cardIndex
                    }

                    if (document.data.getValue("isIgnored") == false || document.id == argsCard.cardId) {
                        cards.add(
                            Card(
                                document.data.getValue("question") as String?,
                                document.data.getValue("answer") as String?,
                                false
                            )
                        )
                        cardIndex++
                    }

                }
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }
}






































/*
class CardFragment : Fragment() {
    private val argsCard: CardFragmentArgs by navArgs()
    private var cards = ArrayList<Card>()
    private lateinit var database : FirebaseFirestore
    private val cardText = view?.findViewById<TextView>(R.id.cardTextView)
    var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //ToDo: load deck and choose starting index
            //int = it.getString(ARG_PARAM1)
            //cards = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        index = 0

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Loads non-ignored cards into a local navigatable ArrayList
        loadDeck()

        // Previous Card button
        val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener{
            if (index == 0) {
                index = cards.size -1
            }
            else {
                index -= 1
            }
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question

        }

        // Next Card Button
        val nextCardBtn = view.findViewById<Button>(R.id.nextCardButton)
        nextCardBtn.setOnClickListener{
            if (index == cards.size -1) {
                index = 0
            }
            else {
                index += 1
            }
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
        }

        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener{
            if (view?.findViewById<TextView>(R.id.cardTextView)?.text == cards[index].question) {
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].answer
            }
            else {
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
            }
        }
    }

    //Loads deck from firestore
    private fun loadDeck() {
        database = FirebaseFirestore.getInstance()
        Log.e("Load Decks LOG", database.toString())
        var cardIndex = 0

        database.collection("Decks").document(argsCard.deckId.toString()).collection("cards").get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")

                    if (document.id == argsCard.cardId) {
                        index = cardIndex
                    }

                    if (document.data.getValue("isIgnored") == false || document.id == argsCard.cardId) {
                        cards.add(
                            Card(
                                document.data.getValue("question") as String?,
                                document.data.getValue("answer") as String?,
                                false
                            )
                        )
                        cardIndex++
                    }

                }
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }
 */











































































































































/*val cardText = view.findViewById<TextView>(R.id.cardTextView)
cardText.setText(cards[index].question)*/

/*// Previous Card button
val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
previousCardBtn.setOnClickListener{
    if (index == 0) {
        index = cards.size -1
    }
    else {
        index -= 1
    }
    cardText?.text = cards[index].question
}

// Next Card Button
val nextCardBtn = view.findViewById<Button>(R.id.nextCardButton)
nextCardBtn.setOnClickListener{
    if (index == cards.size -1) {
        index = 0
    }
    else {
        index += 1
    }
    cardText?.text = cards[index].question
}

// Flip Card Button
val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
flipCardBtn.setOnClickListener{
    if (cardText?.text == cards[index].question) {
        cardText?.text = cards[index].answer
    }
    else {
        cardText?.text = cards[index].question
    }
}*/