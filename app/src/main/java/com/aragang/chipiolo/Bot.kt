package com.aragang.chipiolo

class Bot(val name: String, cards: List<Card>) {
    val cards = cards.toMutableList()

//    imprimir cartas pa tin
    fun printCards() {
        println("$name cards:")
        this.cards.forEach { card ->
            println(card)
        }
    }
}