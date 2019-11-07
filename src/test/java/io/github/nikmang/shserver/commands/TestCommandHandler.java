package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.ClientHandler;
import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.MessageController;
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
    private ClientHandler mockClientHandler;

    @BeforeEach
    public void setup() {
        GameController mockGameController = mock(GameController.class);
        mockMessageController = mock(MessageController.class);
        mockClientHandler = mock(ClientHandler.class);

        when(mockClientHandler.getUser()).thenReturn(new User("test-user", null));

        testHandler = new CommandHandler(mockMessageController, mockGameController);
    }

    @Test
    public void testInvalidCommand() throws IOException {
        //Given
        //When
        testHandler.runCommand(mockClientHandler, "this-is-not-a-command");

        //Then
        verify(mockMessageController, times(1))
                .sendMessageAsServer(eq(mockClientHandler.getUser()), eq("INVALID COMMAND"), eq(false));
    }
}
