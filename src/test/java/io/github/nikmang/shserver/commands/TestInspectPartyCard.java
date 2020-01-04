package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.ClientController;
import io.github.nikmang.shserver.messaging.MessageController;
import io.github.nikmang.shserver.client.Party;
import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestInspectPartyCard {

    private MessageController mockMessageController;
    private GameController mockGameController;
    private InspectPartyCard testInspectPartCardCommand;
    private User testUser;

    @BeforeEach
    public void setup() {
        mockMessageController = mock(MessageController.class);
        mockGameController = mock(GameController.class);

        testUser = new User("test-user", null);
        testInspectPartCardCommand = new InspectPartyCard(mockMessageController, mockGameController);

        when(mockGameController.getPresident()).thenReturn(testUser);
        when(mockGameController.getGameState()).thenReturn(GameState.SPECIAL);
    }

    @Test
    public void testWrongGameState() throws IOException {
        //Given
        when(mockGameController.getGameState()).thenReturn(GameState.VOTING);

        //When
        testInspectPartCardCommand.execute(testUser, new String[]{"test_user"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("Not in the correct game state currently. Try again."),
                eq(false));

        verify(mockGameController, never()).inspectUserPartyCard(any());

    }

    @Test
    public void testUserNotPresident() throws IOException {
        //Given
        when(mockGameController.getPresident()).thenReturn(new User("not_user", null));

        //When
        testInspectPartCardCommand.execute(testUser, new String[]{"test_user"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("You must be president to view others cards."),
                eq(false));

        verify(mockGameController, never()).inspectUserPartyCard(any());
    }

    @Test
    public void testNoArgumentsPosted() throws IOException {
        //Given
        //When
        testInspectPartCardCommand.execute(testUser, new String[0]);

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("You must supply target user name."),
                eq(false));

        verify(mockGameController, never()).inspectUserPartyCard(any());
    }

    @Test
    public void testGetSelfParty() throws IOException {
        //Given
        //When
        testInspectPartCardCommand.execute(testUser, new String[]{"test-USER"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("Don't waste this on yourself..."),
                eq(false));

        verify(mockGameController, never()).inspectUserPartyCard(any());
    }

    @Test
    public void testNoneParty() throws IOException {
        //Given
        when(mockGameController.inspectUserPartyCard(any())).thenReturn(Party.NONE);

        //When
        testInspectPartCardCommand.execute(testUser, new String[]{"new_user"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("User not found try again."),
                eq(false));

        verify(mockGameController, times(1)).inspectUserPartyCard(any());
    }

    @Test
    public void testSuccess() throws IOException {
        //Given
        when(mockGameController.inspectUserPartyCard(any())).thenReturn(Party.LIBERAL);

        //When
        testInspectPartCardCommand.execute(testUser, new String[]{"new_user"});

        //Then
        verify(mockMessageController, times(1)).sendMessageAsServer(
                eq(testUser),
                eq("new_user's party card is: LIBERAL"),
                eq(false));

        verify(mockMessageController, times(1)).broadcastAsServer(
                eq("test-user has inspected new_user's party card"));

        verify(mockGameController, times(1)).inspectUserPartyCard(any());
    }
}
