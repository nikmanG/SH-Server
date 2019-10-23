package io.github.nikmang.shserver.handlers;

import io.github.nikmang.shserver.controllers.GameController;
import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.commands.Command;
import io.github.nikmang.shserver.commands.DrawCards;
import io.github.nikmang.shserver.commands.Invalid;
import io.github.nikmang.shserver.commands.Quit;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private Map<String, Command> cmds;
    private Command invalid;

    public CommandHandler(MessageController messageController, GameController gameController) {
        cmds = new HashMap<>();
        cmds.put("quit", new Quit(messageController));
        cmds.put("draw", new DrawCards(messageController, gameController));

        invalid = new Invalid(messageController);
    }

    public void runCommand(ClientHandler handler, String cmd) throws IOException {
        String[] parts = cmd.split("\\s");

        Command target = cmds.getOrDefault(parts[0].substring(1), invalid);

        target.execute(handler, Arrays.stream(parts).filter(x -> !x.equals(parts[0])).toArray(String[]::new));
    }
}
