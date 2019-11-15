package io.github.nikmang.shserver.client;

import io.github.nikmang.shserver.commands.CommandHandler;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.MessageController;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Runnable that is controlling each client thread.
 * Also contains master set of users.
 */
public class ClientHandler implements Runnable {
    private static final Set<User> users;
    private static final MessageController msgContoller;
    private static final CommandHandler cmdHandler;
    private static final GameController gameController;

    private Socket socket;
    private User user;
    private boolean enabled;
    private DataInputStream input;

    static {
        users = new HashSet<>();
        gameController = new GameController(5); //TODO: dynamically allocate it
        msgContoller = new MessageController(users);
        cmdHandler = new CommandHandler(msgContoller, gameController);
    }

    //TODO: this only exists for testing, may need to change some stuff around
    public static Set<User> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    public static User getUserByName(String userName) {
        return ClientHandler
                .getUsers()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(userName))
                .findFirst().orElse(null);
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.enabled = true;

        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            this.input = new DataInputStream(socket.getInputStream());

            this.user = new User("", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String name;
            msgContoller.sendMessageAsServer(user, "Welcome to Secret Hitler Chat Edition.\n" +
                    "Original game by Goat, Wolf, & Cabbage: https://www.secrethitler.com/", true);
            msgContoller.sendMessageAsServer(user, "Enter your username", false);
            // Adding user to server
            do {
                name = input.readUTF();
                enabled = attemptRegister(name);

                if (!enabled) {
                    msgContoller.sendMessageAsServer(
                            user,
                            "Name already in play or not valid.\nMust be alphanumeric and optionally contain an underscore.",
                            false);
                }
            } while(!enabled);

            msgContoller.updateUserList(String.format("%s has joined the server!", user.getName()));
            System.out.printf("%s has been added to server%n", name);

            // Actual server loop
            while (enabled) {
                String msg = input.readUTF();
                System.out.printf("%s: %s%n", user.getName(), msg);

                if (msg.startsWith("/")) {
                    cmdHandler.runCommand(this, msg);
                } else {
                    msgContoller.broadcast(user, msg);
                }
            }
        } catch (IOException e) {
            System.out.printf("%s has logged off%n", user.getName());
            e.printStackTrace();
        } finally {
            users.remove(user);
            msgContoller.updateUserList(String.format("%s has left the server!", user.getName()));
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
    public boolean attemptRegister(String name) {
        user.setName(name);

        if(!name.matches("^\\w{1,16}$") || name.equalsIgnoreCase("SERVER") ) {
            return false;
        }

        synchronized (users) {
            return users.add(user);
        }
    }

    /**
     * Closes all connections the user has and removes them from the list of active players.
     *
     * @throws IOException If socket already closed or null.
     */
    public void closeConnection() throws IOException {
        synchronized (users) {
            users.remove(user);
        }

        close();
        enabled = false;
    }

    private void close() throws IOException {
        input.close();
        user.close();
        socket.close();
    }
}
