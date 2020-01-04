package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.messaging.MessageController;

/**
 * Called whenever a command that is nonexistent is attempted to be run by a client.
 */
class Invalid extends Command {

    Invalid(MessageController messageController) {
        super(messageController);
    }

    @Override
    public void execute(User user, String[] args) {
        this.getMessageController().sendMessageAsServer(user, "INVALID COMMAND", false);
    }
}
