package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.handlers.ClientHandler;

import java.io.IOException;

public abstract class Command {

    private MessageController messageController;

    public Command(MessageController messageController) {
        this.messageController = messageController;
    }

    public abstract void execute(ClientHandler clientHandler, String[] args) throws IOException;

    protected MessageController getMessageController() {
        return messageController;
    }
}
