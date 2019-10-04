package io.github.nikmang.shserver.handlers.commands;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.handlers.ClientHandler;

import java.io.IOException;

public abstract class Command {

    public abstract void execute(ClientHandler handler, User user, String[] args) throws IOException;

}
