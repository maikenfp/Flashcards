package com.example.mobil.model

data class Card(
    val id : Int,
    var question : String,
    var answer : String,
    val deckId : Int,
    val isIgnored : Boolean
    )
