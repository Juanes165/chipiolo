package com.aragang.chipiolo

import android.util.Log

class Bot(val name: String, cards: List<Card>) {
    var cards = cards.toMutableList()
    var isPlanted = false

    // imprimir cartas del bot
    fun printCards() {
        println("$name cards:")
        this.cards.forEach { card ->
            println(card)
        }
    }

    // elegir carta
//    fun pickCard(allCards: MutableList<Card>) {
//        val pickedCard = allCards[0]
//        this.cards.add(pickedCard)
//        allCards.removeAt(0)
//    }

    fun pickCard(allCards: MutableList<Card>): Card? {

    // si no har cartas en el mazo devuelve null delo contrario sigue con el codigo
//        if (allCards.isEmpty()) {
//            return null
//        }

        val pickedCard = allCards[0]
        this.cards.add(pickedCard)
//        allCards.removeAt(0)
        return dropLeastFavorableCard()
    }



    // tirar carta
    fun dropCard(): Card {
        val droppedCard = this.cards[0]
        this.cards.removeAt(0)
        return droppedCard
    }

    // plantar
    fun plant() {
        this.isPlanted = true
    }


    fun dropLeastFavorableCard(): Card? {
        // Group the cards by suit
        val groupedCards = cards.groupBy { it.suit }
        var leastFavorableCard = listOf<Card>()
        var minimo = -1

        // Iterate over each group
        for ((_, group) in groupedCards) {
            val sum = group.sumOf { it.value }

            // If the sum is between 27 and 31, change isPlanted to true and continue
            if (sum in 27..31) {
                isPlanted = true
                return null
            }

            // Sort the group in ascending order based on their value
            val sortedGroup = group.sortedBy { it.value }

            // If the sum is less than 27, remove the card with the lowest value
            // If the sum is more than 31, remove the card with the highest value

            if (minimo == -1 || sum < minimo) {
                minimo = sum
                leastFavorableCard = sortedGroup
                Log.d("Bot", "minimo: $minimo")
            }
        }

//        println score
        Log.d("Bot", "score: $minimo")

        // Remove the least favorable card from the bot's cards
        cards.remove(leastFavorableCard.first())

        // Return the removed card
        return leastFavorableCard.first()
    }
}