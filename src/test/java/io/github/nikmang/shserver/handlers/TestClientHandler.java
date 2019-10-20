package io.github.nikmang.shserver.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Socket;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class TestClientHandler {

    private ClientHandler testClientHandler;

    @BeforeEach
    public void setup() {
        Socket mockSocket = mock(Socket.class);

        testClientHandler = new ClientHandler(mockSocket);
    }
}
