package io.github.nikmang.shserver.handlers;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.handlers.commands.Command;
import io.github.nikmang.shserver.handlers.commands.Invalid;
import io.github.nikmang.shserver.handlers.commands.Quit;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CommandHandler {

    INSTANCE;

    private Map<String, Command> cmds;
    private Command invalid;

    CommandHandler() {
        cmds = new HashMap<>();
        cmds.put("quit", new Quit());

        invalid = new Invalid();
    }

    public void runCommand(ClientHandler handler, User user, String cmd) throws IOException {
        String[] parts = cmd.split("\\s");

        Command target = cmds.getOrDefault(parts[0].substring(1), invalid);

        target.execute(handler, user, Arrays.stream(parts).filter(x -> !x.equals(parts[0])).toArray(String[]::new));
    }
}
