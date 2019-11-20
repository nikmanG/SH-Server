package io.github.nikmang.shserver.client;

import io.github.nikmang.shserver.commands.CommandHandler;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.messaging.MessageController;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Runnable that is controlling each client thread.<br>
 * Also contains master set of users.
 */
class ClientHandler implements Runnable {
    private MessageController msgController;
    private Socket socket;
    private User user;
    private boolean enabled;
    private DataInputStream input;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.enabled = true;
        this.msgController = ClientController.INSTANCE.getMessageController();

        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            this.input = new DataInputStream(socket.getInputStream());

            this.user = new User("", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the main loop between server and client.
     */
    public void run() {
        try {
            String name;
            msgController.sendMessageAsServer(user, "Welcome to Secret Hitler Chat Edition.\n" +
                    "Original game by Goat, Wolf, & Cabbage: https://www.secrethitler.com/", true);
            msgController.sendMessageAsServer(user, "Enter your username", false);
            // Adding user to server
            do {
                name = input.readUTF();
                enabled = attemptRegister(name);

                if (!enabled) {
                    msgController.sendMessageAsServer(
                            user,
                            "Name already in play or not valid.\nMust be alphanumeric and optionally contain an underscore.",
                            false);
                }
            } while(!enabled);

            msgController.updateUserList(String.format("%s has joined the server!", user.getName()));
            System.out.printf("%s has been added to server%n", name);

            // Actual server loop
            while (enabled) {
                String msg = input.readUTF();
                System.out.printf("%s: %s%n", user.getName(), msg);

                if (msg.startsWith("/")) {
                    ClientController.INSTANCE.executeCommand(this, msg);
                } else {
                    msgController.broadcast(user, msg);
                }
            }
        } catch (IOException e) {
            System.out.printf("%s has logged off%n", user.getName());
            e.printStackTrace();
        } finally {
            ClientController.INSTANCE.closeConnection(user);
            msgController.updateUserList(String.format("%s has left the server!", user.getName()));
        }
    }

    /**
     * Retrieves the user for this instance.
     *
     * @return user instance.
     */
    public User getUser() {
        return user;
    }

    /**
     * Attempts to register the user for the clienthandler with given name.
     *
     * @param name Chosen name of the player. Case sensitive.
     * @return <b>true</b> if name is alphanumeric (with optional underscore) and is unique to the game.
     */
    private boolean attemptRegister(String name) {
        user.setName(name);

        return ClientController.INSTANCE.attemptRegister(this, user, name);
    }

    /**
     * Closes all connections the user has and removes them from the list of active players.
     *
     * @throws IOException If socket already closed or null.
     */
    void closeConnection() throws IOException {
        close();
        enabled = false;
    }

    private void close() throws IOException {
        input.close();
        user.close();
        socket.close();
    }
}
