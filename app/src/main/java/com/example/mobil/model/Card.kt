package com.example.mobil.model

import com.google.firebase.firestore.DocumentId

data class Card(
    var question : String? = null,
    var answer : String? = null,
    var isIgnored : Boolean? = false,
    @DocumentId
    val docId : String? = null
    )
