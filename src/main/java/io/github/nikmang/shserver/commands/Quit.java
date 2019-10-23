package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.handlers.ClientHandler;

import java.io.IOException;

public class Quit extends Command {

    public Quit(MessageController messageController) {
        super(messageController);
    }
    
    @Override
    public void execute(ClientHandler handler, String[] args) throws IOException {
        User executor = handler.getUser();

        this.getMessageController().sendMessageAsServer(executor, "You have left the server", false);
        this.getMessageController().broadcastAsServer(String.format("%s has left the server!", executor.getName()));

        handler.closeConnection();
    }

}
