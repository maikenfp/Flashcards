package com.example.mobil.model

import com.google.firebase.firestore.DocumentId

data class Deck(
    var title : String? = null,
    @DocumentId
    var docId : String? = null

    //var cards : List<Card>? = null
    )


