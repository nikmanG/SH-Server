package io.github.nikmang.shserver.handlers;

import io.github.nikmang.shserver.controllers.GameController;
import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.User;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

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
        gameController = new GameController();
        msgContoller = new MessageController(users);
        cmdHandler = new CommandHandler(msgContoller, gameController);
    }

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.enabled = true;

        try {
            this.input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            user = new User("", outputStream);

            // Adding user to server
            while (enabled) {
                msgContoller.sendMessageAsServer(user, "Enter your username", false);

                String name = input.readUTF();

                user = new User(name, outputStream);

                synchronized (users) {
                    if (!name.equalsIgnoreCase("SERVER") && users.add(user)) {
                        msgContoller.broadcastAsServer(String.format("%s has joined the server!", user.getName()));

                        System.out.printf("%s has been added to server%n", name);
                        break;
                    }
                }

                msgContoller.sendMessageAsServer(user, "Name already in play", false);
            }

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

            msgContoller.broadcastAsServer(String.format("%s has left the server!", user.getName()));
        } finally {
            users.remove(user);
        }
    }

    public User getUser() {
        return user;
    }

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
