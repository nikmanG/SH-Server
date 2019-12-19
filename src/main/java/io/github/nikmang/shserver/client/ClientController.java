package io.github.nikmang.shserver.client;

import io.github.nikmang.shserver.commands.CommandHandler;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.messaging.MessageController;

import java.io.IOException;
import java.util.*;

public enum ClientController {

    INSTANCE;

    private final Map<User, ClientHandler> users;
    private final MessageController msgContoller;
    private final CommandHandler cmdHandler;
    private final GameController gameController;

    ClientController(){
        users = new HashMap<>();
        gameController = new GameController(5); //TODO: dynamically allocate it
        msgContoller = new MessageController(users.keySet());
        cmdHandler = new CommandHandler(msgContoller, gameController);
    }

    /**
     * Retrieves an immutable set of current players.<br>
     * TODO: this only exists for testing, may need to change some stuff around.
     *
     * @return Immutable hashset of the players in the game.
     */
    public Set<User> getUsers() {
        return Collections.unmodifiableSet(users.keySet());
    }

    /**
     * Retrieves a user by their username. The name is case insensitive.
     *
     * @param userName User name of targeted user.
     * @return {@link User} object of the target. <b>null</b> if no player found.
     */
    public User getUserByName(String userName) {
        return users
                .keySet()
                .stream()
                .filter(x -> x.getName().equalsIgnoreCase(userName))
                .findFirst().orElse(null);
    }

    /**
     * Attempts to register the user for the clienthandler with given name.
     *
     * @param clientHandler {@link ClientHandler} instance of the user requesting to register.
     * @return <b>true</b> if name is alphanumeric (with optional underscore) and is unique to the game.
     */
    public boolean attemptRegister(ClientHandler clientHandler) {
        User user = clientHandler.getUser();

        if(!user.getName().matches("^\\w{1,16}$") || user.getName().equalsIgnoreCase("SERVER") ) {
            return false;
        }

        synchronized (users) {
            if(users.containsKey(user)) {
                return false;
            } else {
                users.put(user, clientHandler);
                return true;
            }
        }
    }

    /**
     * Retrieves the message controller for the game.
     *
     * @return {@link MessageController} instance.
     */
    public MessageController getMessageController() {
        return msgContoller;
    }

    /**
     * Execute command from a user.
     *
     * @param clientHandler Instance of clienthandler that sends the command.
     * @param cmd Command with arguments with / as starting e.g) <b>/test arg1 arg2 arg3...</b>
     *
     * @throws IOException If clienthandler has closed output stream.
     */
    public void executeCommand(ClientHandler clientHandler, String cmd) throws IOException {
        cmdHandler.runCommand(clientHandler.getUser(), cmd);
    }

    public synchronized void closeConnection(User user) {
        ClientHandler clientHandler = users.remove(user);

        if(clientHandler == null)
            return;

        //This should execute regardless of crash.
        try {
            clientHandler.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
