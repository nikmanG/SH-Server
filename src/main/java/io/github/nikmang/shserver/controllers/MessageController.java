package io.github.nikmang.shserver.controllers;

import io.github.nikmang.shserver.User;

import java.io.IOException;
import java.util.Set;

public class MessageController {

    private Set<User> users;

    public MessageController(Set<User> userList) {
        this.users = userList;
    }

    public void broadcastAsServer(String message) {
        broadcast("SERVER", message);
    }

    public void broadcast(User sender, String message) {
        broadcast(sender.getName(), message);
    }

    public void sendMessage(User sender, User recipient, String message) {
        sendMessage(sender.getName(), recipient, message, false);
        sendMessage(sender.getName(), sender, message, false);
    }

    public void sendMessageAsServer(User recipient, String message, boolean headless) {
        sendMessage("SERVER", recipient, message, headless);
    }

    private void sendMessage(String sender, User recipient, String message, boolean anonymous) {
        try {
            if(anonymous) {
                recipient.sendAnonymousMessage(message);
            } else {
                recipient.sendMessage(sender, message);
            }
        } catch (IOException e) {
            System.err.printf("Could not send message to %s%n", recipient.getName());
        }
    }

    private void broadcast(String sender, String message) {
        for (User user : users) {
            try {
                user.sendMessage(sender, message);
            } catch (IOException e) {
                System.err.printf("%s could not receive the message%n", user.getName());
            }
        }
    }
}
