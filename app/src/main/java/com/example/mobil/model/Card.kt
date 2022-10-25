package com.example.mobil.model

data class Card(
    val id : Int? = null,
    var question : String? = null,
    var answer : String? = null,
    val deckId : Int? = null,
    val isIgnored : Boolean
    )
