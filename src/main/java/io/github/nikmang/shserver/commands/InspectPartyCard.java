package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.MessageController;
import io.github.nikmang.shserver.client.ClientHandler;

import java.io.IOException;

public class InspectPartyCard extends Command {

    public InspectPartyCard(MessageController messageController) {
        super(messageController);
    }

    @Override
    public void execute(ClientHandler clientHandler, String[] args) throws IOException {

    }
}
