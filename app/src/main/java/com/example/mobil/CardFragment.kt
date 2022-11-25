package com.example.mobil

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.navigation.fragment.navArgs
import com.example.mobil.model.Card
import com.google.firebase.firestore.*
import java.util.*
import kotlin.collections.ArrayList

class CardFragment : Fragment() {
    private val argsCard: CardFragmentArgs by navArgs()
    private var cards = ArrayList<Card>()
    private lateinit var database: FirebaseFirestore
    private lateinit var frontAnimator: AnimatorSet
    private lateinit var backAnimator: AnimatorSet
    private lateinit var fastFrontAnimator: AnimatorSet
    private lateinit var fastBackAnimator: AnimatorSet
    private lateinit var tts: TextToSpeech
    private var isFront = true
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        // Text-To_speech Language
        tts = TextToSpeech(context) { status -> if (status != TextToSpeech.ERROR) { tts.language = Locale.US } }

        index = 0
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Loads non-ignored cards into a local navigable ArrayList
        loadDeck()

        // Card Front and Back
        val cardFront = view.findViewById<CardView>(R.id.card_view_question)
        val cardBack = view.findViewById<CardView>(R.id.card_view_answer)

        // Animators
        frontAnimator = AnimatorInflater.loadAnimator(context, R.animator.front_animation) as AnimatorSet
        backAnimator = AnimatorInflater.loadAnimator(context, R.animator.back_animation) as AnimatorSet

        // Fast animators to instantly flip card
        fastFrontAnimator = AnimatorInflater.loadAnimator(context, R.animator.fast_front_animation) as AnimatorSet
        fastBackAnimator = AnimatorInflater.loadAnimator(context, R.animator.fast_back_animation) as AnimatorSet

        // Text-To-Speech Button
        val ttsButtonFront = view.findViewById<Button>(R.id.ttsButtonQuestion)
        val ttsButtonBack = view.findViewById<Button>(R.id.ttsButtonAnswer)
        ttsButtonFront.setOnClickListener { ttsFunction(view) }
        ttsButtonBack.setOnClickListener { ttsFunction(view) }

        // Hamburger Menu Button
        val menuFront : ImageView = view.findViewById(R.id.cardHamburgerMenuQuestion)
        val menuBack : ImageView = view.findViewById(R.id.cardHamburgerMenuAnswer)
        menuFront.setOnClickListener{ popupMenu(menuFront)}
        menuBack.setOnClickListener{ popupMenu(menuBack) }

        // Previous Card button
        val previousCardBtn = view.findViewById<Button>(R.id.previousCardButton)
        previousCardBtn.setOnClickListener {

            // Previous Index
            if (index == 0) { index = cards.size - 1 }
            else { index -= 1 }

            // Previous card will show question first
            if (isFront) { showCard() }
            else {
                flipToQuestionFast(cardFront,cardBack)
                isFront = true
            }

            // Stop Text-To-Speech
            tts.stop()
        }

        // Next Card Button
        val nextCardBtn = view.findViewById<Button>(R.id.nextCardButton)
        nextCardBtn.setOnClickListener {

            // Next Index
            if (index == cards.size - 1) { index = 0 }
            else { index += 1 }

            // Next card will show question first
            if (isFront) { showCard() }
            else {
                flipToQuestionFast(cardFront,cardBack)
                isFront = true
            }

            // Stop Text-To-Speech
            tts.stop()
        }


        // Flip Card Button
        val flipCardBtn = view.findViewById<Button>(R.id.flipCardButton)
        flipCardBtn.setOnClickListener {
            // Flip to answer
            if (isFront) {
                showCard()
                flipToAnswer(cardFront,cardBack)
            }
            // Flip to question
            else {
                showCard()
                flipToQuestion(cardFront,cardBack)
            }

            // Stop Text-To-Speech
            tts.stop()
        }

    }

    // Text-To-Speech Functionality
    private fun ttsFunction(view: View){

        // Text to be spoken
        val toSpeak: String =
            if (isFront) { view.findViewById<TextView>(R.id.cardTextViewQuestion).text.toString() }
            else { view.findViewById<TextView>(R.id.cardTextViewAnswer).text.toString() }

        // Speak the text, QUEUE_FlUSH flushes the TTS queue when pressed
        Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show()
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "1")

    }

    // Hamburger Menu
    private fun popupMenu(menuView : View) {

        val popupMenu = PopupMenu(menuView.context, menuView)
        popupMenu.inflate(R.menu.card_hamburger_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){

                // Ignore Button Functionality
                R.id.cardIgnore->{
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
                    showCard()
                    tts.stop()
                    true
                }

                // Edit Button Functionality
                R.id.cardEdit->{

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
                        showCard()
                        dialog.dismiss()
                    }

                    addCardDialog.setNegativeButton("Cancel") {
                            dialog,_->
                        dialog.dismiss()
                    }
                    addCardDialog.create()
                    addCardDialog.show()
                    tts.stop()

                    true
                }

                // Delete Button Functionality
                R.id.cardDelete->{
                    val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)

                    val deleteDialog = android.app.AlertDialog.Builder(context)
                    deleteDialog.setView(inflater)

                    deleteDialog.setPositiveButton("Delete") {
                            dialog,_->

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
                        showCard()
                        dialog.dismiss()
                        tts.stop()
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

    //Loads deck from Firestore
    private fun loadDeck() {
        database = FirebaseFirestore.getInstance()
        cards = ArrayList()
        var cardIndex = 0
        var cardQuestion = ""

        // Gets the database collection
        database.collection("Decks").document(argsCard.deckId.toString()).collection("cards").get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    // Displays the question which the user clicked on in DeckFragment, regardless if it is ignored or not
                    if (document.id == argsCard.cardId) {
                        index = cardIndex
                        cardQuestion = document.data.getValue("question").toString()
                    }

                    // Loads the database collection into the local arraylist
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

                // Shuffles the local arraylist
                if (argsCard.shuffle) {
                    cards.shuffle()
                    cardIndex = 0
                    for (card in cards) {
                        if (card.question == cardQuestion) {
                            index = cardIndex
                        }
                        cardIndex++
                    }
                }

                // Display the card
                showCard()
            }

            // Logs the error
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error getting documents: ", exception)
            }
    }

    // Animation to flip to the answer
    private fun flipToAnswer(cardFront: CardView, cardBack:CardView) {
        frontAnimator.setTarget(cardFront)
        backAnimator.setTarget(cardBack)
        frontAnimator.start()
        backAnimator.start()
        cardBack.bringToFront()
        isFront = false
    }

    // Animation to flip to the question
    private fun flipToQuestion(cardFront: CardView, cardBack:CardView) {
        frontAnimator.setTarget(cardBack)
        backAnimator.setTarget(cardFront)
        frontAnimator.start()
        backAnimator.start()
        cardFront.bringToFront()
        isFront = true
    }

    // Instant animation to flip to the question
    private fun flipToQuestionFast(cardFront: CardView, cardBack:CardView) {
        fastFrontAnimator.setTarget(cardBack)
        fastBackAnimator.setTarget(cardFront)
        // DoOnEnd is necessary to not spoil the answer of the next card
        fastFrontAnimator.doOnEnd { showCard() }
        fastFrontAnimator.start()
        fastBackAnimator.start()
        cardFront.bringToFront()
        isFront = true

    }

    // Shows the correct card and symbol
    private fun showCard() {
        view?.findViewById<TextView>(R.id.cardTextViewQuestion)?.text = cards[index].question
        view?.findViewById<TextView>(R.id.cardTextViewAnswer)?.text = cards[index].answer


        // Checks if card is ignored, to determine if we are displaying the "ignored" icon
        val cardImageQuestion = view?.findViewById<ImageView>(R.id.cardBackgroundImageViewQuestion)
        val cardImageAnswer = view?.findViewById<ImageView>(R.id.cardBackgroundImageViewAnswer)
        if(cards[index].isIgnored == true) {
            cardImageQuestion?.setImageResource(R.drawable.ic_baseline_ignore_24)
            cardImageAnswer?.setImageResource(R.drawable.ic_baseline_ignore_24)
        }
        else {
            cardImageQuestion?.setImageResource(R.drawable.ic_question_mark)
            cardImageAnswer?.setImageResource(R.drawable.ic_exclamation_mark)
        }
    }

    // When fragment pauses
    override fun onPause() {
        tts.stop()
        super.onPause()
    }

    // When fragment is destroyed
    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}