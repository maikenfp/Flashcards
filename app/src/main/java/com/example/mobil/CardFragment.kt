package com.example.mobil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.example.mobil.model.Card
import com.google.firebase.firestore.*
import kotlin.collections.ArrayList

class CardFragment : Fragment() {
    private val argsCard: CardFragmentArgs by navArgs()
    private var cards = ArrayList<Card>()
    private lateinit var database: FirebaseFirestore
    private val cardText = view?.findViewById<TextView>(R.id.cardTextView)
    var index = 0

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

        // Loads non-ignored cards into a local navigatable ArrayList
        loadDeck()

        // Previous Card button
        val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener {
            if (index == 0) {
                index = cards.size - 1
            } else {
                index -= 1
            }
            showCard(true)

        }

        // Next Card Button
        val nextCardBtn = view.findViewById<Button>(R.id.nextCardButton)
        nextCardBtn.setOnClickListener {
            if (index == cards.size - 1) {
                index = 0
            } else {
                index += 1
            }
            showCard(true)
        }

        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener {
            if (view.findViewById<TextView>(R.id.cardTextView)?.text == cards[index].question) {
                showCard(false)
            } else {
                showCard(true)
            }
        }

        var menu : ImageView = view.findViewById(R.id.cardHamburgerMenu)
        menu.setOnClickListener{ popupMenu(menu) }

    }

    private fun popupMenu(menuView : View) {
        //val db = FirebaseFirestore.getInstance()
        val popupMenu = PopupMenu(menuView.context, menuView)
        popupMenu.inflate(R.menu.card_hamburger_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.cardIgnore->{
                    Log.e("CARD HAMBURGER MENU","IGNORE")
                    // guaranteed
                    val cardID = cards[index].docId
                    var ignored = cards[index].isIgnored

                    ignored = !ignored!!


                    val card = hashMapOf(
                        "question" to cards[index].question,
                        "answer" to cards[index].answer,
                        "isIgnored" to ignored
                    )
                    database.collection("Decks")
                        .document(argsCard.deckId.toString())
                        .collection("cards")
                        .document(cardID.toString())
                        .set(card)

                    Log.e("IGNORED", ignored.toString())
                    if (ignored) {
                        cards.removeAt(index)
                        if (index == 0) {
                            index = cards.size - 1
                        } else {
                            index--
                        }
                    }
                    else {
                        cards[index].isIgnored = ignored

                    }
                    showCard(true)
                    true
                }
                R.id.cardEdit->{
                    Log.e("CARD HAMBURGER MENU","EDIT")
                    val inflater = LayoutInflater.from(context).inflate(R.layout.add_card, null)
                    val addQuestion = inflater.findViewById<EditText>(R.id.enter_question)
                    val addAnswer = inflater.findViewById<EditText>(R.id.enter_answer)

                    addQuestion.setText(cards[index].question)
                    addAnswer.setText(cards[index].answer)

                    val addCardDialog = android.app.AlertDialog.Builder(context)
                    addCardDialog.setView(inflater)

                    addCardDialog.setPositiveButton("Save") {
                            dialog,_->
                        val cardID = cards[index].docId
                        val question = addQuestion.text.toString()
                        val answer = addAnswer.text.toString()
                        val card = hashMapOf(
                            "question" to question,
                            "answer" to answer,
                            "isIgnored" to cards[index].isIgnored,
                        )
                        database.collection("Decks")
                            .document(argsCard.deckId.toString())
                            .collection("cards")
                            .document(cardID.toString())
                            .set(card)


                        cards[index] = Card(question, answer, cards[index].isIgnored, cardID)
                        showCard(true)
                        dialog.dismiss()
                    }

                    addCardDialog.setNegativeButton("Cancel") {
                            dialog,_->
                        dialog.dismiss()
                    }
                    addCardDialog.create()
                    addCardDialog.show()

                    true
                }
                R.id.cardDelete->{
                    Log.e("CARD HAMBURGER MENU","DELETE")
                    val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)

                    val deleteDialog = android.app.AlertDialog.Builder(context)
                    deleteDialog.setView(inflater)

                    deleteDialog.setPositiveButton("Delete") {
                            dialog,_->

                        Log.e("DELETING", cards[index].docId.toString())
                        database.collection("Decks")
                            .document(argsCard.deckId.toString())
                            .collection("cards")
                            .document(cards[index].docId.toString())
                            .delete()
                        cards.removeAt(index)
                        if (index == 0) {
                            index = cards.size - 1
                        } else {
                            index -= 1
                        }
                        showCard(true)
                        dialog.dismiss()
                    }

                    deleteDialog.setNegativeButton("Cancel"){
                            dialog,_->
                        dialog.dismiss()
                    }
                    deleteDialog.create()
                    deleteDialog.show()
                    true
                }
                else -> true
            }
        }
        popupMenu.show()
    }



    //Loads deck from firestore
    private fun loadDeck() {
        database = FirebaseFirestore.getInstance()
        Log.e("Load Decks LOG", database.toString())
        cards = ArrayList<Card>()
        var cardIndex = 0
        var cardQuestion = ""

        database.collection("Decks").document(argsCard.deckId.toString()).collection("cards").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")

                    if (document.id == argsCard.cardId) {
                        index = cardIndex
                        cardQuestion = document.data.getValue("question").toString()
                    }

                    if (document.data.getValue("isIgnored") == false || document.id == argsCard.cardId) {
                        cards.add(
                            Card(
                                document.data.getValue("question") as String?,
                                document.data.getValue("answer") as String?,
                                document.data.getValue("isIgnored") as Boolean?,
                                document.id
                            )
                        )
                        cardIndex++
                    }
                }
                if (argsCard.shuffle == true) {
                    cards.shuffle()
                    cardIndex = 0
                    for (card in cards) {
                        Log.e("Card ID", card.docId.toString())
                        if (card.question == cardQuestion) {
                            index = cardIndex
                        }
                        cardIndex++
                    }
                }
                showCard(true)
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    private fun showCard(isQuestion: Boolean) {
        if (isQuestion == true){
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
        }
        else {
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].answer
        }

        // Checks if card is ignored, to determine if we are displaying the "ignored" icon
        var cardImage = view?.findViewById<ImageView>(R.id.cardIgnoreImageView)
        if(cards[index].isIgnored == true) {
            cardImage?.setImageResource(R.drawable.ic_baseline_ignore_24)
        }
        else if (view?.findViewById<TextView>(R.id.cardTextView)?.text == cards[index].question) {
            cardImage?.setImageResource(R.drawable.ic_question_mark)
        }
        else {
            cardImage?.setImageResource(R.drawable.ic_exclamation_mark)
        }
    }

}

