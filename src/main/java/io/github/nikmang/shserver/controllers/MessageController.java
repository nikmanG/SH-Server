package io.github.nikmang.shserver.controllers;

import io.github.nikmang.shserver.JsonPacketBuilder;
import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.game.GameDeck;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageController {

    private Set<User> users;

    public MessageController(Set<User> userList) {
        this.users = userList;
    }

    /**
     * Broadcasts message with SERVER as sender to all users connected.
     *
     * @param message Message to be sent as the server.
     */
    public void broadcastAsServer(String message) {
        broadcast(new JsonPacketBuilder().withMessage(message).withSender("SERVER"));
    }

    /**
     * Broadcasts message with sender name included to all users connected.
     *
     * @param sender The sender of the message.
     * @param message Message to be sent as the server.
     */
    public void broadcast(User sender, String message) {
        broadcast(new JsonPacketBuilder().withSender(sender.getName()).withMessage(message));
    }

    /**
     * Send message to particular user as an individual sender.
     *
     * @param sender User who is sending the message.
     * @param recipient The user who is the targeted recipient of the message.
     * @param message The actual string message.
     */
    public void sendMessage(User sender, User recipient, String message) {
        sendMessage(sender.getName(), recipient, message, false);
        sendMessage(sender.getName(), sender, message, false);
    }

    /**
     * Send message to particular user as the SERVER user.
     *
     * @param recipient The user who is the targeted recipient of the message.
     * @param message The actual string message.
     * @param headless If <b>true</b> then "SERVER" as name will not be sent.
     */
    public void sendMessageAsServer(User recipient, String message, boolean headless) {
        sendMessage("SERVER", recipient, message, headless);
    }

    /**
     * Sends the card information to a given user.
     * If message is set as <i>null</i> then there will be no accompanying message or sender.
     *
     * @param recipient User that will receive the card information.
     * @param cards The cards that are to be sent.
     * @param message Optional accompanying message.
     */
    public void sendCards(User recipient, List<GameDeck.Card> cards, String message) {
        JsonPacketBuilder packet = new JsonPacketBuilder()
                .withFollowingCards(cards);

        if(message != null) {
            packet.withMessage(message).withSender("SERVER");
        }

        try {
            recipient.sendMessage(packet);
        } catch (IOException e) {
            System.err.printf("Could not send message to %s%n", recipient.getName());
        }
    }

    /**
     * Sends an update of users to all clients.
     * If message is set as <i>null</i> then there will be no accompanying message or sender.
     *
     * @param message Optional message to broadcast to users along with  actual user information.
     */
    public void updateUserList(String message) {
        JsonPacketBuilder packet = new JsonPacketBuilder()
                .withFollowingUsers(users.stream().map(User::getName).collect(Collectors.toList()));

        if(message != null) {
            packet.withMessage(message).withSender("SERVER");
        }

        broadcast(packet);
    }

    private void sendMessage(String sender, User recipient, String message, boolean anonymous) {
        try {
            if (anonymous) {
                recipient.sendMessage(new JsonPacketBuilder().withMessage(message));
            } else {
                recipient.sendMessage(new JsonPacketBuilder().withMessage(message).withSender(sender));
            }
        } catch (IOException e) {
            System.err.printf("Could not send message to %s%n", recipient.getName());
        }
    }

    private void broadcast(JsonPacketBuilder jsonMessage) {
        for (User user : users) {
            try {
                user.sendMessage(jsonMessage);
            } catch (IOException e) {
                System.err.printf("%s could not receive the message%n", user.getName());
            }
        }
    }
}
