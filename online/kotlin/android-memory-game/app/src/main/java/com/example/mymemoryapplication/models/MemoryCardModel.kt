package com.example.mymemoryapplication.models

data class MemoryCardModel(
    val identifier: Int,
    val imageUrl:String? = null,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)