package io.github.nikmang.shserver;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Handler implements Runnable {
    private static final Set<User> users;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    static {
        users = new HashSet<>();
    }

    public Handler(Socket socket) {
        this.socket = socket;

        try {
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        User u = new User("", output);

        try {
            // Adding user to server
            while (true) {
                u.sendMessage("SERVER", "Enter your username");

                String name = input.readUTF();

                u = new User(name, output);

                synchronized (users) {
                    if (!name.equalsIgnoreCase("SERVER") && users.add(u)) {
                        broadast("SERVER", String.format("%s has joined the server!", u.getName()));

                        System.out.printf("%s has been added to server%n", name);
                        break;
                    }
                }

                output.writeUTF("Name already in play\n");
            }

            // Actual server loop
            while (true) {
                String msg = input.readUTF();
                System.out.printf("%s: %s%n", u.getName(), msg);

                if(msg.equalsIgnoreCase("/quit")){
                    synchronized (users) {
                        users.remove(u);
                    }
                    
                    u.sendMessage("SERVER", "GOODBYE");
                    broadast("SERVER", String.format("%s has left the server!", u.getName()));

                    close();
                    return;
                }

                broadast(u.getName(), msg);
            }
        } catch (IOException e) {
            System.out.printf("%s has logged off%n", u.getName());

            broadast("SERVER", String.format("%s has left the server!", u.getName()));
        } finally {
            users.remove(u);
        }
    }

    private void broadast(String sender, String message) {
        for (User user : users) {
            try {
                user.sendMessage(sender, message);
            } catch (IOException e) {
                System.err.printf( "%s could not receive the message", user.getName());
            }
        }
    }

    public void close() throws IOException {
        input.close();
        output.close();
        socket.close();
    }
}
