package io.github.nikmang.shserver.game.configurations;

import io.github.nikmang.shserver.game.GameBoardEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerConfiguration {

    private PlayerConfiguration config;

    @BeforeEach
    public void setup() {
        config = new PlayerConfiguration(true, 0, 0);
    }

    @Test
    public void testQuestionAddition() {
        //Given
        //When
        boolean b1 = config.addGameEffect(1, GameBoardEffect.SEE_PARTY_CARD);
        boolean b2 = config.addGameEffect(5, GameBoardEffect.CHOOSE_NEXT_PRESIDENT);

        //Then
        assertTrue(b1);
        assertFalse(b2);

        assertEquals(GameBoardEffect.SEE_PARTY_CARD, config.getPieceEffect(1));
        assertEquals(GameBoardEffect.KILL, config.getPieceEffect(5));
    }

    @Test
    public void testGetInvalidEffect() {
        //Given
        //When
        GameBoardEffect effect = config.getPieceEffect(1);

        //Then
        assertEquals(GameBoardEffect.NONE, effect);
    }
}
