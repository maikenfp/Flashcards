package com.example.mobil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.example.mobil.adapter.CardsAdapter
import com.example.mobil.model.Card
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Use parameters?
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CardFragment : Fragment() {
    //testing ********************
    private val argsDeck: DeckFragmentArgs by navArgs()
    private val argsCard: CardFragmentArgs by navArgs()
    //testing ********************
    private var cards = ArrayList<Card>()
    private lateinit var database : FirebaseFirestore




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

        val db = Firebase.firestore
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        database = FirebaseFirestore.getInstance()
        val cardCollection : CollectionReference = db.collection("Decks").document(argsDeck.docId.toString()).collection("cards")

        val card = cardCollection.document(argsCard.docId.toString())
        Log.d("TAG", "cardId: ${cardCollection}")
        cardCollection.get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()
            for (document in it.result)
                result.append(document.data.getValue("question")).append("")

            val cardText = view.findViewById<TextView>(R.id.cardTextView)
            cardText.setText(result)
        }


        val cardId = card.get()
        val cardQuestion = cardId



/*        //ToDo: Load deck instead of creating cards here
        cards.add(Card("Card 0 Question", "Card 0 Answer", false))
        cards.add(Card("Card 1 Question", "Card 1 Answer", false))
        cards.add(Card("Card 2 Question", "Card 2 Answer", false))
        cards.add(Card("Card 3 Question", "Card 3 Answer", false))
        cards.add(Card("Card 4 Question", "Card 4 Answer", false))

        val cardText = view.findViewById<TextView>(R.id.cardTextView)
        cardText.setText(cards[index].question)


        // Previous Card button
        val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener{
            if (index == 0) {
                index = cards.size -1
            }
            else {
                index -= 1
            }
            cardText.setText(cards[index].question)

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
            cardText.setText(cards[index].question)
        }




        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener{
            if (cardText.text == cards[index].question) {
                cardText.setText(cards[index].answer)
            }
            else {
                cardText.setText(cards[index].question)
            }
        }
        */




        /*        //ToDo: Load deck instead of creating cards here
        cards.add(Card("Card 0 Question", "Card 0 Answer", false))
        cards.add(Card("Card 1 Question", "Card 1 Answer", false))
        cards.add(Card("Card 2 Question", "Card 2 Answer", false))
        cards.add(Card("Card 3 Question", "Card 3 Answer", false))
        cards.add(Card("Card 4 Question", "Card 4 Answer", false))

        val cardText = view.findViewById<TextView>(R.id.cardTextView)
        cardText.setText(cards[index].question)


        // Previous Card button
        val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener{
            if (index == 0) {
                index = cards.size -1
            }
            else {
                index -= 1
            }
            cardText.setText(cards[index].question)




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
            cardText.setText(cards[index].question)
        }

        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener{
            if (cardText.text == cards[index].question) {
                cardText.setText(cards[index].answer)
            }
            else {
                cardText.setText(cards[index].question)
            }
        }
        */
        return view
    }

    private fun eventChangeListener(adapter: CardsAdapter) {
        database = FirebaseFirestore.getInstance()
        Log.e("Load Decks LOG", database.toString())
        database.collection("Decks").document(argsDeck.docId.toString()).collection("cards")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
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


























































































