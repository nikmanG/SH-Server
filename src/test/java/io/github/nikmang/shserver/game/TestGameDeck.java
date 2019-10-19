package io.github.nikmang.shserver.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class TestGameDeck {

    private GameDeck testDeck;

    @BeforeEach
    public void setup() {
        testDeck = new GameDeck();
    }

    @Test
    public void testDefaultDeckGeneration() {
        //Given
        //When
        List<GameDeck.Card> allCards = testDeck.getCards(17);

        long liberal = allCards.stream().filter(x -> x == GameDeck.Card.LIBERAL).count();
        long fascist = allCards.stream().filter(x -> x == GameDeck.Card.FASCIST).count();

        //Then
        assertEquals(17, allCards.size());
        assertEquals(6, liberal);
        assertEquals(11, fascist);
    }

    @Test
    public void testGetCard() {
        //Given
        //When
        GameDeck.Card card = testDeck.getCard();
        List<GameDeck.Card> allCards = testDeck.getCards(17);

        //Then
        assertEquals(16, allCards.size());
        assertNotNull(card);
    }

    @Test
    public void testGetCardAfterReplenish() {
        //Given
        //When
        GameDeck.Card card = testDeck.getCard();
        testDeck.addCardToDiscardPile(card);
        List<GameDeck.Card> allCards = testDeck.getCards(17);

        //Then
        assertEquals(17, allCards.size());
        assertNotNull(card);
    }

    @Test
    public void testCardAppearance() {
        //Given
        //When
        String liberalCard = GameDeck.Card.LIBERAL.toCardPrint();
        String fascistCard = GameDeck.Card.FASCIST.toCardPrint();

        //Then
        assertEquals(" ---------\n|F        |\n|         |\n| FASCIST |\n|         |\n|        F|\n ---------",
                fascistCard);
        assertEquals(" ---------\n|L        |\n|         |\n| LIBERAL |\n|         |\n|        L|\n ---------",
                liberalCard);
    }
}
