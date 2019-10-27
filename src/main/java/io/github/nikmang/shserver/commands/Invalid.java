package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.client.ClientHandler;

import java.io.IOException;

public class Invalid extends Command {

    Invalid(MessageController messageController) {
        super(messageController);
    }

    @Override
    public void execute(ClientHandler handler, String[] args) throws IOException {
        this.getMessageController().sendMessageAsServer(handler.getUser(), "INVALID COMMAND", false);
    }
}