package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.handlers.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TestQuit {

    private ClientHandler mockClientHandler;
    private MessageController mockMessageController;
    private Quit testQuitCommand;
    private User testUser;

    @BeforeEach
    public void setup() {
        mockClientHandler = mock(ClientHandler.class);
        mockMessageController = mock(MessageController.class);

        testUser = new User("test-user", null);
        testQuitCommand = new Quit(mockMessageController);

        when(mockClientHandler.getUser()).thenReturn(testUser);
    }

    @Test
    public void testRunThrough() throws IOException {
        //Given
        //When
        testQuitCommand.execute(mockClientHandler, new String[0]);

        //Then
        verify(mockMessageController, times(1))
                .sendMessageAsServer(eq(testUser), eq("You have left the server"), eq(false));
        verify(mockMessageController, times(1))
                .broadcastAsServer(eq("test-user has left the server!"));
    }
}
