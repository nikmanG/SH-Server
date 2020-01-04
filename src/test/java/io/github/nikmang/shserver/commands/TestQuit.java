package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.messaging.MessageController;
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

    private MessageController mockMessageController;
    private Quit testQuitCommand;
    private User testUser;

    @BeforeEach
    public void setup() {
        mockMessageController = mock(MessageController.class);

        testUser = new User("test-user", null);
        testQuitCommand = new Quit(mockMessageController);
    }

    @Test
    public void testRunThrough() throws IOException {
        //Given
        //When
        testQuitCommand.execute(testUser, new String[0]);

        //Then
        verify(mockMessageController, times(1))
                .sendMessageAsServer(eq(testUser), eq("You have left the server"), eq(false));
        verify(mockMessageController, times(1))
                .broadcastAsServer(eq("test-user has left the server!"));
    }
}
