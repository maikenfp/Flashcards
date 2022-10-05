package com.example.mobil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mobil.model.Card

// TODO: Use parameters?
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CardFragment : Fragment() {
    private var index = 0
    private var cards = ArrayList<Card>()



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

        //ToDo: Load deck instead of creating cards here
        cards.add(Card(0, "Card 0 Question", "Card 0 Answer", 0, false))
        cards.add(Card(1, "Card 1 Question", "Card 1 Answer", 0, false))
        cards.add(Card(2, "Card 2 Question", "Card 2 Answer", 0, false))
        cards.add(Card(3, "Card 3 Question", "Card 3 Answer", 0, false))
        cards.add(Card(4, "Card 4 Question", "Card 4 Answer", 0, false))

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
        return view
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