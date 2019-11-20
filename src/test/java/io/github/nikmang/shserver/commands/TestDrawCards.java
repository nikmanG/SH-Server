package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.MessageController;
import io.github.nikmang.shserver.game.Card;
import io.github.nikmang.shserver.client.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        when(mockGameController.getPresident()).thenReturn(testUser);
        when(mockGameController.getChancellor()).thenReturn(testUser);
    }

    @Test
    public void testProperFormatSingleCard() throws IOException {
        //Given
        when(mockGameController.getCardsInPlay()).thenReturn(Collections.singletonList(Card.LIBERAL));

        //When
        testDrawCardsCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1))
                .sendCards(eq(testUser),
                        eq(Collections.singletonList(Card.LIBERAL)),
                        eq(null));
    }

    @Test
    public void testWhenPresidentIsNotSet() throws IOException {
        //Given
        when(mockGameController.getPresident()).thenReturn(null);

        //When
        testDrawCardsCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("Need to wait for president and chancellor to be set before drawing"),
                eq(false));

        verify(mockGameController, never()).getCardsInPlay();
    }

    @Test
    public void testWhenPresidentIsNotUser() throws IOException {
        //Given
        when(mockGameController.getPresident()).thenReturn(new User("test-user2", null));

        //When
        testDrawCardsCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("You must be president or chancellor to view cards"),
                eq(false));

        verify(mockGameController, never()).getCardsInPlay();
    }

    @Test
    public void testProperFormatManyCards() throws IOException {
        //Given
        when(mockGameController.getCardsInPlay())
                .thenReturn(Arrays.asList(Card.LIBERAL, Card.FASCIST, Card.LIBERAL));

        //When
        testDrawCardsCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1))
                .sendCards(eq(testUser),
                        eq(Arrays.asList(Card.LIBERAL, Card.FASCIST, Card.LIBERAL)),
                        eq(null));
    }
}
