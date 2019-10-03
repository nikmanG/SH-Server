package io.github.nikmang.shserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SHServer {

    public static void main(String[] args) throws IOException {
        System.out.println("Server is starting up");
        ExecutorService pool = Executors.newFixedThreadPool(500);

        ServerSocket server = new ServerSocket(5990);

        while(true) {
            pool.execute(new Handler(server.accept()));
        }
    }
}
