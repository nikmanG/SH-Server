package io.github.nikmang.shserver;

import io.github.nikmang.shserver.client.ClientController;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that is used to start the server.
 */
public class SHServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Server is starting up");
        ExecutorService pool = Executors.newFixedThreadPool(10);

        ServerSocket server = new ServerSocket(5990);
        System.out.println("Server initialized");

        while (true) {
            pool.execute(ClientController.INSTANCE.createClientHandler(server.accept()));
        }
    }
}
