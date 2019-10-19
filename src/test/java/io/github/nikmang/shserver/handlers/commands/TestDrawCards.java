package io.github.nikmang.shserver.handlers.commands;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.controllers.GameController;
import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.game.GameDeck;
import io.github.nikmang.shserver.handlers.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TestDrawCards {

    private ClientHandler mockClientHandler;
    private MessageController mockMessageController;
    private GameController mockGameController;
    private DrawCards testDrawCardsCommand;
    private User testUser;

    @BeforeEach
    public void setup() {
        mockClientHandler = mock(ClientHandler.class);
        mockMessageController = mock(MessageController.class);
        mockGameController = mock(GameController.class);

        testUser = new User("test-user", null);
        testDrawCardsCommand = new DrawCards(mockMessageController, mockGameController);

        when(mockClientHandler.getUser()).thenReturn(testUser);
    }

    @Test
    public void testFailWhenNoCards() {
        //Given
        when(mockGameController.getCardsInPlay()).thenReturn(Collections.emptyList());

        //When
        assertThrows(AssertionError.class, () -> testDrawCardsCommand.execute(mockClientHandler, new String[0]));

        //Then
        //throws error
    }

    @Test
    public void testProperFormatSingleCard() throws IOException {
        //Given
        when(mockGameController.getCardsInPlay()).thenReturn(Collections.singletonList(GameDeck.Card.LIBERAL));

        //When
        testDrawCardsCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1))
                .sendMessageAsServer(eq(testUser),
                        eq(" ---------\n" +
                        "|L        |\n" +
                        "|         |\n" +
                        "| LIBERAL |\n" +
                        "|         |\n" +
                        "|        L|\n" +
                        " ---------\n" +
                        "    [1]    \t"),
                        eq(true));
    }

    @Test
    public void testProperFormatManyCards() throws IOException {
        //Given
        when(mockGameController.getCardsInPlay())
                .thenReturn(Arrays.asList(GameDeck.Card.LIBERAL, GameDeck.Card.FASCIST, GameDeck.Card.LIBERAL));

        //When
        testDrawCardsCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1))
                .sendMessageAsServer(eq(testUser),
                        eq(" ---------\t ---------\t ---------\n" +
                                "|L        |\t|F        |\t|L        |\n" +
                                "|         |\t|         |\t|         |\n" +
                                "| LIBERAL |\t| FASCIST |\t| LIBERAL |\n" +
                                "|         |\t|         |\t|         |\n" +
                                "|        L|\t|        F|\t|        L|\n" +
                                " ---------\t ---------\t ---------\n" +
                                "    [1]    \t    [2]    \t    [3]    \t"),
                        eq(true));
    }
}
