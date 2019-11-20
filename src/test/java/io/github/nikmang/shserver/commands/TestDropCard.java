package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.messaging.MessageController;
import io.github.nikmang.shserver.client.ClientHandler;
import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.Card;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestDropCard {

    private DropCard testDropCard;
    private User mockSender;
    private MessageController mockMessageController;
    private ClientHandler mockClientHandler;
    private GameController mockGameController;

    @BeforeEach
    public void setup() {
        mockClientHandler = mock(ClientHandler.class);
        mockMessageController = mock(MessageController.class);
        mockGameController = mock(GameController.class);
        mockSender = new User("SENDER", null);
        testDropCard = new DropCard(mockMessageController, mockGameController);

        when(mockClientHandler.getUser()).thenReturn(mockSender);

        when(mockGameController.getPresident()).thenReturn(mockSender);
        when(mockGameController.getGameState()).thenReturn(GameState.CARD_CHOICE);
        when(mockGameController.removeCardFromPlay(anyInt())).thenReturn(true);
        when(mockGameController.getCardsInPlay()).thenReturn(Arrays.asList(Card.LIBERAL, Card.FASCIST, Card.LIBERAL));
    }

    @Test
    public void testPlayerNotPresident() throws IOException {
        //Given
        when(mockGameController.getPresident()).thenReturn(new User("NOT SENDER", null));

        //When
        testDropCard.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("You must be president to drop cards."),
                eq(false));

        verify(mockGameController, never()).getCardsInPlay();
        verify(mockGameController, never()).getGameState();
        verify(mockGameController, never()).removeCardFromPlay(anyInt());
    }

    @Test
    public void testInvalidGameState() throws IOException {
        //Given
        when(mockGameController.getGameState()).thenReturn(GameState.VOTING);

        //When
        testDropCard.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("Wait until chancellor is set."),
                eq(false));

        verify(mockGameController, never()).getCardsInPlay();
        verify(mockGameController, never()).removeCardFromPlay(anyInt());
    }

    @Test
    public void testNotEnoughCards() throws IOException {
        //Given
        when(mockGameController.getCardsInPlay()).thenReturn(Collections.emptyList());

        //When
        testDropCard.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("Cards have already been dropped. Cannot drop cards again."),
                eq(false));

        verify(mockGameController, never()).removeCardFromPlay(anyInt());
    }

    @Test
    public void testNoArguments() throws IOException {
        //Given
        //When
        testDropCard.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("Need to supply the index of the card you wish to drop."),
                eq(false));

        verify(mockGameController, never()).removeCardFromPlay(anyInt());
    }

    @Test
    public void testNotNumber() throws IOException {
        //Given
        //When
        testDropCard.execute(mockClientHandler, new String[]{"a"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("Need to supply the index of the card you wish to drop."),
                eq(false));

        verify(mockGameController, never()).removeCardFromPlay(anyInt());
    }

    @Test
    public void testWrongNumber() throws IOException {
        //Given
        when(mockGameController.removeCardFromPlay(-6)).thenReturn(false);

        //When
        testDropCard.execute(mockClientHandler, new String[]{"-5"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("Need to supply the index that's 1, 2 or 3."),
                eq(false));

        verify(mockGameController, times(1)).removeCardFromPlay(anyInt());
    }

    @Test
    public void testWorksSingle() throws IOException {
        //Given
        User chancellor = new User("CHANCELLOR", null);
        when(mockGameController.getChancellor()).thenReturn(chancellor);

        //When
        testDropCard.execute(mockClientHandler, new String[]{"2"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(mockSender),
                eq("You have removed the 2 card from the deck."),
                eq(false));

        verify(mockMessageController, times(1))
                .broadcastAsServer(eq("SENDER has dropped a card. CHANCELLOR should now choose which of two to play."));

        verify(mockMessageController, times(1)).sendCards(
                eq(chancellor),
                anyList(),
                eq("Please choose which one to play."));

        verify(mockGameController, times(1)).removeCardFromPlay(eq(1));
    }
}
