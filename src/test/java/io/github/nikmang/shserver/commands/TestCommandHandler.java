package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.messaging.MessageController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCommandHandler {

    private CommandHandler testHandler;
    private MessageController mockMessageController;
    private User mockUser;

    @BeforeEach
    public void setup() {
        GameController mockGameController = mock(GameController.class);
        mockMessageController = mock(MessageController.class);
        testHandler = new CommandHandler(mockMessageController, mockGameController);
        mockUser = new User("Test-User", null);
    }

    @Test
    public void testInvalidCommand() throws IOException {
        //Given
        //When
        testHandler.runCommand(mockUser, "this-is-not-a-command");

        //Then
        verify(mockMessageController, times(1))
                .sendMessageAsServer(eq(mockUser), eq("INVALID COMMAND"), eq(false));
    }
}
