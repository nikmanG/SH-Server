package io.github.nikmang.shserver.game.configurations;

import io.github.nikmang.shserver.game.GameBoardEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerConfigurationFactory {

    @Test
    public void testFiveAndSixPlayerConfiguration() {
        //Given
        //When
        PlayerConfiguration config5 = PlayerConfigurationFactory.getConfigurationOnPlayerCount(5);
        PlayerConfiguration config6 = PlayerConfigurationFactory.getConfigurationOnPlayerCount(6);

        //Then
        assertEquals(2, config5.getFascistCount());
        assertEquals(2, config6.getFascistCount());

        assertEquals(3, config5.getLiberalCount());
        assertEquals(4, config6.getLiberalCount());

        assertTrue(config5.hitlerKnowsFascists());
        assertTrue(config6.hitlerKnowsFascists());

        assertEquals(GameBoardEffect.SEE_TOP_CARDS, config5.getPieceEffect(3));
        assertEquals(GameBoardEffect.KILL, config5.getPieceEffect(4));
        assertEquals(GameBoardEffect.KILL, config5.getPieceEffect(5));

        assertEquals(GameBoardEffect.SEE_TOP_CARDS, config6.getPieceEffect(3));
        assertEquals(GameBoardEffect.KILL, config6.getPieceEffect(4));
        assertEquals(GameBoardEffect.KILL, config6.getPieceEffect(5));
    }

    @Test
    public void testSevenAndEightPlayerConfiguration() {
        //Given
        //When
        PlayerConfiguration config7 = PlayerConfigurationFactory.getConfigurationOnPlayerCount(7);
        PlayerConfiguration config8 = PlayerConfigurationFactory.getConfigurationOnPlayerCount(8);

        //Then
        assertEquals(3, config7.getFascistCount());
        assertEquals(3, config8.getFascistCount());

        assertEquals(4, config7.getLiberalCount());
        assertEquals(5, config8.getLiberalCount());

        assertFalse(config7.hitlerKnowsFascists());
        assertFalse(config8.hitlerKnowsFascists());

        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config7.getPieceEffect(2));
        assertEquals(GameBoardEffect.CHOOSE_NEXT_PRESIDENT, config7.getPieceEffect(3));
        assertEquals(GameBoardEffect.KILL, config7.getPieceEffect(4));
        assertEquals(GameBoardEffect.KILL, config7.getPieceEffect(5));

        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config8.getPieceEffect(2));
        assertEquals(GameBoardEffect.CHOOSE_NEXT_PRESIDENT, config8.getPieceEffect(3));
        assertEquals(GameBoardEffect.KILL, config8.getPieceEffect(4));
        assertEquals(GameBoardEffect.KILL, config8.getPieceEffect(5));
    }

    @Test
    public void testNineAndTenPlayerConfiguration() {
        //Given
        //When
        PlayerConfiguration config9 = PlayerConfigurationFactory.getConfigurationOnPlayerCount(9);
        PlayerConfiguration config10 = PlayerConfigurationFactory.getConfigurationOnPlayerCount(10);

        //Then
        assertEquals(4, config9.getFascistCount());
        assertEquals(4, config10.getFascistCount());

        assertEquals(5, config9.getLiberalCount());
        assertEquals(6, config10.getLiberalCount());

        assertFalse(config9.hitlerKnowsFascists());
        assertFalse(config10.hitlerKnowsFascists());

        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config9.getPieceEffect(1));
        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config9.getPieceEffect(2));
        assertEquals(GameBoardEffect.CHOOSE_NEXT_PRESIDENT, config9.getPieceEffect(3));
        assertEquals(GameBoardEffect.KILL, config9.getPieceEffect(4));
        assertEquals(GameBoardEffect.KILL, config9.getPieceEffect(5));

        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config10.getPieceEffect(1));
        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config10.getPieceEffect(2));
        assertEquals(GameBoardEffect.CHOOSE_NEXT_PRESIDENT, config10.getPieceEffect(3));
        assertEquals(GameBoardEffect.KILL, config10.getPieceEffect(4));
        assertEquals(GameBoardEffect.KILL, config10.getPieceEffect(5));
    }
}
