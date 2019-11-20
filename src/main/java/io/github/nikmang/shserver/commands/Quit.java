package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.ClientController;
import io.github.nikmang.shserver.messaging.MessageController;
import io.github.nikmang.shserver.client.User;

import java.io.IOException;

/**
 * Called to gracefully close the client from server.
 */
class Quit extends Command {

    Quit(MessageController messageController) {
        super(messageController);
    }
    
    @Override
    public void execute(User user, String[] args) throws IOException {
        this.getMessageController().sendMessageAsServer(user, "You have left the server", false);
        this.getMessageController().broadcastAsServer(String.format("%s has left the server!", user.getName()));

        ClientController.INSTANCE.closeConnection(user);
    }

}
