package com.aragang.chipiolo

import androidx.compose.runtime.MutableState

class Bot(val name: String, cards: List<Card>) {
    val cards = cards.toMutableList()
    var lastPlayedCard: Card? = null

//    tomar cartas
    fun takeCard(deck: MutableState<List<Card>>){
        if (deck.value.isNotEmpty()){
            val card = deck.value.random()
            deck.value = deck.value - card
            this.cards.add(card)
        }
    }

    // descartar cartas
    fun discardCard(){
        if (cards.isNotEmpty()){
            lastPlayedCard = cards[0]
            cards.removeAt(0)

        }
    }

    //  Puntuacion del bot
    fun getScore(): Int {
        return cards.sumOf { it.value }
    }



//    imprimir cartas pa tin
    fun printCards() {
        println("$name cards:")
        this.cards.forEach { card ->
            println(card)
        }
    }


    // imprimir la última carta jugada
    fun printLastPlayedCard() {
        println("$name played: $lastPlayedCard")
    }
}