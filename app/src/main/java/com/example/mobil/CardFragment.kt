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

// TODO: Use parameters?
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CardFragment : Fragment() {
    //testing ********************
    private val argsCard: CardFragmentArgs by navArgs()
    //testing ********************
    private var cards = ArrayList<Card>()
    private lateinit var database : FirebaseFirestore
    private val cardText = view?.findViewById<TextView>(R.id.cardTextView)




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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventChangeListener()

        var index = 0
        var cardIndex = 0

        // Previous Card button
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
        }

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

    }

    private fun eventChangeListener() {
        database = FirebaseFirestore.getInstance()
        Log.e("Load Decks LOG", database.toString())
        var index = 0
        var cardIndex = 0

        database.collection("Decks").document(argsCard.deckId.toString()).collection("cards").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")

                    // ToDo: This doesn't prevent the user to click on a ignored card, which will mess up the indexing
                    // Todo: Possible fix is to present the first card directly without the using the arraylist
                    // Todo: Then when you navigate, you cannot navigate back to this card
                    if (document.data.getValue("isIgnored") == false) {
                        cards.add(
                            Card(
                                document.data.getValue("question") as String?,
                                document.data.getValue("answer") as String?,
                                false
                            )
                        )
                        if (document.id == argsCard.cardId) {
                            index = cardIndex
                        }
                        cardIndex++
                        view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }




    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }























}