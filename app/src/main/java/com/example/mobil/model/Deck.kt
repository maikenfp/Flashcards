package com.example.mobil.model

import com.google.firebase.firestore.DocumentId

data class Deck(
    val id : Int? = null,
    var title : String? = null,
    @DocumentId
    val docId: String? = null
    )


