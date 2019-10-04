package io.github.nikmang.shserver.handlers;

import io.github.nikmang.shserver.User;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable {
    private static final Set<User> users;

    private Socket socket;
    private User user;
    private boolean enabled;
    private DataInputStream input;

    static {
        users = new HashSet<>();
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
                user.sendMessage("SERVER", "Enter your username");

                String name = input.readUTF();

                user = new User(name, outputStream);

                synchronized (users) {
                    if (!name.equalsIgnoreCase("SERVER") && users.add(user)) {
                        broadast("SERVER", String.format("%s has joined the server!", user.getName()));

                        System.out.printf("%s has been added to server%n", name);
                        break;
                    }
                }

                user.sendMessage("SERVER", "Name already in play");
            }

            // Actual server loop
            while (enabled) {
                String msg = input.readUTF();
                System.out.printf("%s: %s%n", user.getName(), msg);

                if (msg.startsWith("/")) {
                    CommandHandler.INSTANCE.runCommand(this, user, msg);
                } else {
                    broadast(user.getName(), msg);
                }
            }
        } catch (IOException e) {
            System.out.printf("%s has logged off%n", user.getName());

            broadast("SERVER", String.format("%s has left the server!", user.getName()));
        } finally {
            users.remove(user);
        }
    }

    public void closeConnection() throws IOException {
        synchronized (users) {
            users.remove(user);
        }

        close();
        enabled = false;
    }

    public void broadast(String sender, String message) {
        for (User user : users) {
            try {
                user.sendMessage(sender, message);
            } catch (IOException e) {
                System.err.printf("%s could not receive the message", user.getName());
            }
        }
    }

    private void close() throws IOException {
        input.close();
        user.close();
        socket.close();
    }
}
