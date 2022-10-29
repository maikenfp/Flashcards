package com.example.mobil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.adapter.EditAdapter
import com.example.mobil.model.Card

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
    private var cards = ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        // adapter & recycler
        val editAdapter = EditAdapter(context = MainActivity(), cards)
        val editRecycler = view.findViewById<RecyclerView>(R.id.card_recycler)
        editRecycler.adapter = editAdapter
        editRecycler.layoutManager = LinearLayoutManager(context)

        //ToDo: Load deck instead of creating cards here
        cards.add(Card(0, "Card 0 Question", "Card 0 Answer", 0, false))
        cards.add(Card(1, "Card 1 Question", "Card 1 Answer", 0, false))
        cards.add(Card(2, "Card 2 Question", "Card 2 Answer", 0, false))
        cards.add(Card(3, "Card 3 Question", "Card 3 Answer", 0, false))
        cards.add(Card(4, "Card 4 Question", "Card 4 Answer", 0, false))


        // Delete button
        val deleteBtn = view.findViewById<Button>(R.id.deleteCardBtn)
        deleteBtn.setOnClickListener {
            val inflater = LayoutInflater.from(context).inflate(R.layout.delete_card, null)

            val deleteDialog = android.app.AlertDialog.Builder(context)
            deleteDialog.setView(inflater)

            deleteDialog.setPositiveButton("Delete") {
                    dialog,_->
                for (c in cards){
                    if(c.isIgnored){
                        cards.remove(c)
                        editAdapter.notifyDataSetChanged()
                    }
                }
                dialog.dismiss()
            }

            deleteDialog.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }
            deleteDialog.create()
            deleteDialog.show()
        }


        // Ignore button
        val ignoreBtn = view.findViewById<Button>(R.id.ignoreCardBtn)
        ignoreBtn.setOnClickListener {

        }

        // Select card
        editAdapter.setOnCardClickListener(object : EditAdapter.onCardClickListener{
            override fun onCardClick(position: Int) {
                //ToDo: This now needs to work with fragment
                (activity as DeckActivity).replaceFragment(CardFragment())
                //startActivity(Intent(this@EditFragment.context, CardActivity::class.java))
            }
        })

        return view
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