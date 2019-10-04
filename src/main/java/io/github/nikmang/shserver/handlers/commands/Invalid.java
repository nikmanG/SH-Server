package io.github.nikmang.shserver.handlers.commands;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.handlers.ClientHandler;
import io.github.nikmang.shserver.handlers.CommandHandler;

import java.io.IOException;

public class Invalid extends Command {

    @Override
    public void execute(ClientHandler handler, User user, String[] args) throws IOException {
        user.sendMessage("SERVER", "INVALID COMMAND");
    }
}
