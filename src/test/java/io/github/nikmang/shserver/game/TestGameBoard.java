package io.github.nikmang.shserver.game;

import io.github.nikmang.shserver.client.Party;
import io.github.nikmang.shserver.game.configurations.PlayerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestGameBoard {

    private GameBoard testGameBoard;
    private PlayerConfiguration mockConfig;

    @BeforeEach
    public void setup() {
        mockConfig = mock(PlayerConfiguration.class);
        testGameBoard = new GameBoard(mockConfig);
    }

    @Test
    public void testLiberalMove() {
        //Given
        //When
        GameBoardEffect effect = testGameBoard.playPiece(Card.LIBERAL);
        Party winner = testGameBoard.getWinner();

        //Then
        assertEquals(GameBoardEffect.NONE, effect);
        assertEquals(Party.NONE, winner);
    }

    @Test
    public void testLiberalWin() {
        //Given
        for(int i=0; i<5; i++) {
            testGameBoard.playPiece(Card.LIBERAL);
        }

        //When
        Party winner = testGameBoard.getWinner();

        //Then
        assertEquals(Party.LIBERAL, winner);
    }

    @Test
    public void testFascistMove() {
        //Given
        when(mockConfig.getPieceEffect(anyInt())).thenReturn(GameBoardEffect.SEE_PARTY_CARD);

        //When
        GameBoardEffect effect = testGameBoard.playPiece(Card.FASCIST);
        Party winner = testGameBoard.getWinner();

        //Then
        assertEquals(GameBoardEffect.SEE_PARTY_CARD, effect);
        assertEquals(Party.NONE, winner);
    }

    @Test
    public void testFascistWin() {
        //Given
        for(int i=0; i<6; i++) {
            testGameBoard.playPiece(Card.FASCIST);
        }

        //When
        Party winner = testGameBoard.getWinner();

        //Then
        assertEquals(Party.FASCIST, winner);
    }

    @Test
    public void testDeadlockIncrement() {
        //Given
        //When
        int before = testGameBoard.getDeadlockCount();
        testGameBoard.incrementDeadlock();
        int after = testGameBoard.getDeadlockCount();

        //Then
        assertEquals(0, before);
        assertEquals(1, after);
    }

    @Test
    public void testDeadlockReset() {
        //Given
        testGameBoard.incrementDeadlock();

        //When
        int before = testGameBoard.getDeadlockCount();
        testGameBoard.playPiece(Card.values()[ThreadLocalRandom.current().nextInt(2)]);
        int after = testGameBoard.getDeadlockCount();

        //Then
        assertEquals(1, before);
        assertEquals(0, after);
    }
}
