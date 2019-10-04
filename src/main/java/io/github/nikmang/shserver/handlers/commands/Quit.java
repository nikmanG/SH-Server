package io.github.nikmang.shserver.handlers.commands;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.handlers.ClientHandler;

import java.io.IOException;

public class Quit extends Command {

    @Override
    public void execute(ClientHandler handler, User user, String[] args) throws IOException {
        user.sendMessage("SERVER", "You have left the server");
        handler.broadast("SERVER", String.format("%s has left the server!", user.getName()));
        handler.closeConnection();
    }

}
