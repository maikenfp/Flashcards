package com.example.mobil.model

import com.google.firebase.firestore.DocumentId

data class Card(
    val id : Int? = null,
    var question : String? = null,
    var answer : String? = null,
    val isIgnored : Boolean? = false,
    @DocumentId
    val docId : String? = null
    )
