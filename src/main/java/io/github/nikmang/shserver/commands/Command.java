package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.messaging.MessageController;

import java.io.IOException;

/**
 * Respective command to be executed
 */
public abstract class Command {

    private MessageController messageController;

    public Command(MessageController messageController) {
        this.messageController = messageController;
    }

    /**
     * Called when person sends up a command request (message beginning with /)<br>
     *<br>
     * Example:<br>
     *  <b>/msg user Hello World</b><br>
     *  Above will try find command with <i>msg</i> name and will pass in arguments ['user', 'Hello', 'World']
     *
     * @param user The respective {@link User} object of the sender of the command.
     * @param args Arguments of the command that are split by space. Does not include actual command name.
     *
     * @throws IOException If the input socket is closed or null for recipients.
     */
    public abstract void execute(User user, String[] args) throws IOException;

    protected MessageController getMessageController() {
        return messageController;
    }
}
