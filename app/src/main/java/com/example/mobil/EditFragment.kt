package com.example.mobil

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobil.adapter.EditAdapter
import com.example.mobil.databinding.FragmentEditBinding
import com.example.mobil.model.Card
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {

    // NAV ARGS
    private val args: EditFragmentArgs by navArgs()

    private var _editBinding: FragmentEditBinding? = null
    private val editBinding get() = _editBinding!!

    private var cards = ArrayList<Card>()
    private lateinit var database : FirebaseFirestore

    private val editAdapter = EditAdapter(context = MainActivity(), cards)
    private val selected = editAdapter.selectedCards

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _editBinding = FragmentEditBinding.inflate(layoutInflater)

        // Adapter & Recycler
        val cardsRecycler = editBinding.cardRecycler
        cardsRecycler.layoutManager = LinearLayoutManager(context)
        cardsRecycler.adapter = editAdapter

        eventChangeListener(editAdapter)

        //BUTTONS
        val deleteBtn = editBinding.deleteCardBtn
        val ignoreBtn = editBinding.ignoreCardBtn

        // Delete button
        deleteBtn.setOnClickListener {
            deleteCards()
        }

        // Ignore button
        ignoreBtn.setOnClickListener {
            ignoreCards()
        }
        return editBinding.root
    }

    private fun deleteCards() {
        val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)

        val deleteDialog = AlertDialog.Builder(context)
        deleteDialog.setView(inflater)

        deleteDialog.setPositiveButton("Delete") {
                dialog,_->
            for (card in selected){
                database.collection("Decks")
                    .document(args.deckId.toString())
                    .collection("cards")
                    .document(card.docId.toString())
                    .delete()
                editAdapter.notifyDataSetChanged()
            }
            dialog.dismiss()
            (activity as FlashcardContainer).onSupportNavigateUp()
        }

        deleteDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
        }
        deleteDialog.create()
        deleteDialog.show()
    }

    private fun ignoreCards() {
        for (card in selected){
            Log.e("SELECTED CARD", card.toString())
            var ignored = card.isIgnored
            ignored = !ignored!!
            val cardHash = hashMapOf(
                "question" to card.question,
                "answer" to card.answer,
                "isIgnored" to ignored
            )
            database.collection("Decks")
                .document(args.deckId.toString())
                .collection("cards")
                .document(card.docId.toString())
                .set(cardHash)
        }
        (activity as FlashcardContainer).onSupportNavigateUp()
    }

    private fun eventChangeListener(adapter: EditAdapter) {
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
                        //cards.add(Card(dc.document.data.getValue("question").toString(),dc.document.data.getValue("answer").toString(),dc.document.data.getValue("isIgnored").toString()
                        Log.e("Add Card Error", cards.toString())
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
         * @return A new instance of fragment EditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}