package com.example.mobil

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.adapter.DecksAdapter
import com.example.mobil.model.Card
import com.example.mobil.model.Deck
import com.google.firebase.firestore.*
import java.util.*
import kotlin.collections.ArrayList

class CardFragment : Fragment() {
    private val argsCard: CardFragmentArgs by navArgs()
    private var cards = ArrayList<Card>()
    private lateinit var database: FirebaseFirestore
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
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question

        }

        // Next Card Button
        val nextCardBtn = view.findViewById<Button>(R.id.nextCardButton)
        nextCardBtn.setOnClickListener {
            if (index == cards.size - 1) {
                index = 0
            } else {
                index += 1
            }
            view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
        }

        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener {
            if (view?.findViewById<TextView>(R.id.cardTextView)?.text == cards[index].question) {
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].answer
            } else {
                view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
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
                    true
                }
                R.id.cardEdit->{
                    Log.e("CARD HAMBURGER MENU","EDIT")
                    true
                }
                R.id.cardDelete->{
                    Log.e("CARD HAMBURGER MENU","DELETE")
                    val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)

                    val deleteDialog = android.app.AlertDialog.Builder(context)
                    deleteDialog.setView(inflater)

                    deleteDialog.setPositiveButton("Delete") {
                            dialog,_->

                        Log.e("INDEX", index.toString())
                        Log.e("DELETING", cards[index].docId.toString())
                        database.collection("Decks")
                            .document(argsCard.deckId.toString())
                            .collection("cards")
                            .document(cards[index].docId.toString())
                            .delete()
                        Log.e("B4 REMOVE INDEX", index.toString())
                        cards.removeAt(index)
                        Log.e("AFTER REMOVE INDEX", index.toString())
                        if (index == 0) {
                            index = cards.size - 1
                        } else {
                            index -= 1
                        }
                        Log.e("TextViewUpdate Before", index.toString())
                        view?.findViewById<TextView>(R.id.cardTextView)?.text = cards[index].question
                        Log.e("TextViewUpdate After", index.toString())
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
                                false,
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
/*
                    R.id.deleteDeck->{
                        AlertDialog.Builder(view.context).setTitle("Delete").setIcon(R.drawable.ic_warning).setMessage("Are you sure you want to delete this deck?")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                //Må finne ut hvordan koble til riktig dokument onclick
                                if (position != null) {
                                    db.collection("Decks").document(position.id).delete()
                                }
                                notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
 */




