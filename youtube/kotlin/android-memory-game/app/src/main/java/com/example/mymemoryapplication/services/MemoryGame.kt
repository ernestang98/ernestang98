package com.example.mymemoryapplication.services

import com.example.mymemoryapplication.constants.BoardSize
import com.example.mymemoryapplication.constants.DEFAULT_ICONS
import com.example.mymemoryapplication.models.MemoryCardModel

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?) {

    val memoryCards: List<MemoryCardModel>
    var numPairsFound = 0

    private var numberOfCardFlips = 0

    private var indexOfFirstSelectedCard: Int? = null

    init {
        if (customImages == null) {
            val chosenImageIcons : List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages : List<Int> = (chosenImageIcons + chosenImageIcons).shuffled()
            memoryCards = randomizedImages.map { MemoryCardModel(it) }
        }
        else {
            val randomizedImages : List<String> = (customImages + customImages).shuffled()
            memoryCards = randomizedImages.map { MemoryCardModel(it.hashCode() ,it) }
        }
    }

    fun flipCard(position: Int) : Boolean {
        numberOfCardFlips++
        val card: MemoryCardModel = memoryCards[position]
        var match = false
        if (indexOfFirstSelectedCard == null) {
            restoreCards()
            indexOfFirstSelectedCard = position
        }
        else {
            match = checkIfMatch(indexOfFirstSelectedCard!!, position)
            indexOfFirstSelectedCard = null
        }
        /*
        Case 1: 0 cards flipped over
        Case 2: 1 cards flipped over
        Case 3: 2 cards flipped over
         */
        card.isFaceUp = !card.isFaceUp
        return match
    }

    private fun checkIfMatch(first: Int, second: Int): Boolean {
        if (memoryCards[first].identifier != memoryCards[second].identifier) {
            return false
        }
        memoryCards[first].isMatched = true
        memoryCards[second].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card: MemoryCardModel in memoryCards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return memoryCards[position].isFaceUp
    }

    fun getNumberOfMoves(): Int {
        return numberOfCardFlips / 2
    }

}