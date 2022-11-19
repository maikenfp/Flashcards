package com.example.mobil.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Card(
    var question : String? = null,
    var answer : String? = null,
    // Firebase has a problem with booleans that is starting with "is"
    // Using a getter and setter fixes this issue
    @get:PropertyName("isIgnored")
    @set:PropertyName("isIgnored")
    var isIgnored : Boolean? = false,
    @DocumentId
    val docId : String? = null
    )
