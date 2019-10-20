package io.github.nikmang.shserver.controllers;

import io.github.nikmang.shserver.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestMessageController {

    private MessageController testMessageController;
    private List<User> userList;

    @BeforeEach
    public void setupOnce() {
        userList = new ArrayList<>();
        User mockUser = mock(User.class);
//        when(mockUser.getName()).thenReturn("test-user");

        User mockUser2 = mock(User.class);

        User mockUser3 = mock(User.class);

        userList.addAll(Arrays.asList(mockUser, mockUser2, mockUser3));
        testMessageController = new MessageController(new HashSet<>(userList));
    }

    @Test
    public void testSendMessage() throws IOException {
        //Given
        when(userList.get(0).getName()).thenReturn("test-user");

        //When
        testMessageController.sendMessage(userList.get(0), userList.get(1), "TEST");

        //Then
        verify(userList.get(0), times(1)).sendMessage(eq("test-user"), eq("TEST"));
        verify(userList.get(1), times(1)).sendMessage(eq("test-user"), eq("TEST"));
        verify(userList.get(2), never()).sendMessage(eq("test-user"), eq("TEST"));
    }

    @Test
    public void testSendMessageAsServer() throws IOException {
        //Given
        //When
        testMessageController.sendMessageAsServer(userList.get(0), "TEST", false);

        //Then
        verify(userList.get(0), times(1)).sendMessage(eq("SERVER"), eq("TEST"));
        verify(userList.get(1), never()).sendMessage(eq("SERVER"), eq("TEST"));
        verify(userList.get(2), never()).sendMessage(eq("SERVER"), eq("TEST"));
    }

    @Test
    public void testSendMessageAsServerHeadless() throws IOException {
        //Given
        //When
        testMessageController.sendMessageAsServer(userList.get(0), "TEST", true);

        //Then
        verify(userList.get(0), times(1)).sendAnonymousMessage(eq("TEST"));
        verify(userList.get(1), never()).sendAnonymousMessage(eq("TEST"));
        verify(userList.get(2), never()).sendAnonymousMessage(eq("TEST"));
    }

    @Test
    public void testBroadcast() throws IOException {
        //Given
        when(userList.get(0).getName()).thenReturn("test-user");

        //When
        testMessageController.broadcast(userList.get(0), "TEST");

        //Then
        verify(userList.get(0), times(1)).sendMessage(eq("test-user"), eq("TEST"));
        verify(userList.get(1), times(1)).sendMessage(eq("test-user"), eq("TEST"));
        verify(userList.get(2), times(1)).sendMessage(eq("test-user"), eq("TEST"));
    }

    @Test
    public void testBroadcastAsServer() throws IOException {
        //Given
        //When
        testMessageController.broadcastAsServer("TEST");

        //Then
        verify(userList.get(0), times(1)).sendMessage(eq("SERVER"), eq("TEST"));
        verify(userList.get(1), times(1)).sendMessage(eq("SERVER"), eq("TEST"));
        verify(userList.get(2), times(1)).sendMessage(eq("SERVER"), eq("TEST"));
    }
}
