package com.aragang.chipiolo
import org.junit.Test

class GameScreenTest {
    @Test
    fun getUserScoreTest() {
        val userScore1 = getUserScores(listOf<Card>(Card(1, 10, "Clubs", R.drawable.trebol_card_j), Card(2, 2, "Clubs",  R.drawable.trebol_card_2 ), Card(3, 6, "Clubs", R.drawable.trebol_card_6)))
        val userScore2 = getUserScores(listOf<Card>(Card(1, 5, "Hearts", R.drawable.corazon_card_5), Card(2, 7, "Diamonds",  R.drawable.diamante_card_7 )))
        val userScore3 = getUserScores(listOf<Card>(Card(1, 5, "Hearts", R.drawable.corazon_card_5), Card(2, 7, "Diamonds",  R.drawable.diamante_card_7 ), Card(3, 10, "Hearts",  R.drawable.corazon_card_k ), Card(4, 11, "Spades",  R.drawable.pica_card_1 )))

        assert(userScore1 == listOf<Int>(18,0,0,0))
        assert(userScore2 == listOf<Int>(0,7,5,0))
        assert(userScore3 == listOf<Int>(0,7,15,11))
    }
}
