package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.MessageController;
import io.github.nikmang.shserver.client.ClientHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for running and parsing commands.
 */
public class CommandHandler {

    private Map<String, Command> cmds;
    private Command invalid;

    /**
     * Constructor to CommandHandler.
     *
     * @param messageController Main instance of message controller used in the game.
     * @param gameController Main instance of game controller used in game.
     */
    public CommandHandler(MessageController messageController, GameController gameController) {
        cmds = new HashMap<>();
        cmds.put("quit", new Quit(messageController));
        cmds.put("draw", new DrawCards(messageController, gameController));
        cmds.put("inspect", new DrawCards(messageController, gameController));
        cmds.put("party", new InspectPartyCard(messageController, gameController));
        cmds.put("drop", new DropCard(messageController, gameController));

        invalid = new Invalid(messageController);
    }

    /**
     * Attempts to run a command server-side and returns JSON response (if applicable) to the sender.
     *
     * @param handler Handler of the sender.
     * @param cmd Command without the preceding slash.
     *
     * @throws IOException thrown if receiver of message cannot receive it.
     */
    public void runCommand(ClientHandler handler, String cmd) throws IOException {
        String[] parts = cmd.split("\\s");

        Command target = cmds.getOrDefault(parts[0].substring(1), invalid);

        target.execute(handler, Arrays.stream(parts).filter(x -> !x.equals(parts[0])).toArray(String[]::new));
    }
}
