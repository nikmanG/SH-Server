package io.github.nikmang.shserver.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestClientHandler {

    private static List<ClientHandler> clients;
    private ClientHandler testClientHandler;

    @BeforeAll
    public static void setupOnce() {
        clients = Collections.synchronizedList(new ArrayList<>());
    }

    @BeforeEach
    public void setup() throws IOException {
        Socket mockSocket = setupMockSocket();
        testClientHandler = new ClientHandler(mockSocket);
    }

    @AfterEach
    public void tearDown() throws IOException {
        synchronized (clients) {
            for (ClientHandler c : clients) {
                c.closeConnection();
            }
        }

        clients.clear();
    }

    @Test
    public void testSingleRegisterAttempt() {
        //Given
        clients.add(testClientHandler);

        //When
        testClientHandler.attemptRegister("test_user");

        //Then
        assertEquals(1, ClientHandler.getUsers().size());
    }

    @Test
    public void testInvalidNameRegisterAttempt() {
        //Given
        //When
        boolean b1 = testClientHandler.attemptRegister("test-user");
        boolean b2 = testClientHandler.attemptRegister("/testuser");
        boolean b3 = testClientHandler.attemptRegister("TEST!");

        //Then
        assertFalse(b1);
        assertFalse(b2);
        assertFalse(b3);

        assertEquals(0, ClientHandler.getUsers().size());
    }

    @Test
    public void testRegisterMultipleDifferentNameAttempt() throws InterruptedException {
        //Given
        ExecutorService pool = Executors.newFixedThreadPool(500);

        //When
        for(int i=0; i<100; i++) {
            final int nameItr = i;

            pool.execute(() -> {
                try {
                    ClientHandler c = new ClientHandler(setupMockSocket());
                    c.attemptRegister("test_user" + nameItr);

                    clients.add(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(2000L, TimeUnit.MILLISECONDS);

        //Then
        assertEquals(100, ClientHandler.getUsers().size());
    }

    @Test
    public void testRegisterMultipleSameNameAttempt() throws InterruptedException {
        //Given
        ExecutorService pool = Executors.newFixedThreadPool(500);

        //When
        for(int i=0; i<100; i++) {
            pool.execute(() -> {
                try {
                    ClientHandler c = new ClientHandler(setupMockSocket());
                    c.attemptRegister("test_user");

                    clients.add(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(2000L, TimeUnit.MILLISECONDS);

        //Then
        assertEquals(1, ClientHandler.getUsers().size());
    }

    @Test
    public void testQuitSingleAttempt() throws IOException {
        //Given
        testClientHandler.attemptRegister("test_user");

        //When
        testClientHandler.closeConnection();

        //Then
        assertEquals(0, ClientHandler.getUsers().size());
    }

    @Test
    public void testQuitMultipleAttempt() throws IOException, InterruptedException {
        //Given
        ExecutorService pool = Executors.newFixedThreadPool(500);

        for(int i=0; i<100; i++) {
            final int nameItr = i;

            pool.execute(() -> {
                try {
                    ClientHandler c = new ClientHandler(setupMockSocket());
                    c.attemptRegister("test_user" + nameItr);

                    clients.add(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        //When
        for(int i=0; i<50; i++) {
            final int nameItr = i;

            pool.execute(() -> {
                try {
                    clients.get(nameItr).closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(2000L, TimeUnit.MILLISECONDS);

        //Then
        assertEquals(50, ClientHandler.getUsers().size());
    }

    private Socket setupMockSocket() throws IOException {
        Socket mockSocket = mock(Socket.class);

        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[1024]));

        return mockSocket;
    }
}
