package com.example.mobil

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobil.adapter.DecksAdapter
import com.example.mobil.databinding.FragmentMainBinding
import com.example.mobil.model.Deck
import com.google.firebase.firestore.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {

    private var _mainBinding: FragmentMainBinding? = null
    private val mainBinding get() = _mainBinding!!

    private var decks = ArrayList<Deck>()
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
        _mainBinding = FragmentMainBinding.inflate(layoutInflater)

        return mainBinding.root
    }

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        return view
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter & Recycler
        val decksRecycler = mainBinding.deckRecycler
        decksRecycler.layoutManager = LinearLayoutManager(context)
        val decksAdapter = DecksAdapter(context = MainActivity(), decks)
        decksRecycler.adapter = decksAdapter

        eventChangeListener(decksAdapter)

        //BUTTONS
        val addDeckBtn = mainBinding.addDeckButton

        addDeckBtn.setOnClickListener{

            val inflater = LayoutInflater.from(context).inflate(R.layout.add_deck, null)
            val addText = inflater.findViewById<EditText>(R.id.addDeckName)

            val addDialog = AlertDialog.Builder(context)
            addDialog.setView(inflater)

            addDialog.setPositiveButton("Ok"){
                    dialog,_->

                val deckTitle = addText.text.toString()

                val deck = hashMapOf(
                    "id" to 0,
                    "title" to deckTitle
                )

                database.collection("Decks")
                    .add(deck)

                decksAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            addDialog.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }
            addDialog.create()
            addDialog.show()
        }

        decksAdapter.setOnItemClickListener(object : DecksAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                /*val currentId = database.collection("Decks").document().id
                val directions = MainFragmentDirections.actionMainFragmentToDeckFragment(currentId)
                val navHostFragment = (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
                val navController = navHostFragment.navController

                navController.navigate(directions)

                Log.e("NAVIGATE Error", currentId)
                */

                //(activity as MainActivity).replaceFragment(DeckFragment())

                //testing ********************
                val currentId = database.collection("Decks").document().id

                val navHostFragment = (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
                val navController = navHostFragment.navController

                for (d in decks) {
                    if (currentId == d.docId){
                        val directions = MainFragmentDirections.actionMainFragmentToDeckFragment(currentId)
                        navController.navigate(directions)
                    }
                    else {
                        Log.e("IT DIDNT WORK", currentId)
                    }
                }
                //testing ********************

            }
        })
    }

    private fun eventChangeListener(adapter: DecksAdapter) {
        database = FirebaseFirestore.getInstance()
        database.collection("Decks").
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
                        decks.add(dc.document.toObject(Deck::class.java))
                        Log.e("Load Decks LOG", decks.toString())
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
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}