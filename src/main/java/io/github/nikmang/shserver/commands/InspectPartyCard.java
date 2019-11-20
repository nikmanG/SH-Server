package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.MessageController;
import io.github.nikmang.shserver.client.ClientHandler;
import io.github.nikmang.shserver.client.Party;
import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.game.GameState;

import java.io.IOException;
import java.util.Optional;

/**
 * Called to inspect an individuals card.
 * Will not work on senders own card.
 *
 * Current phase of the game must be {@link io.github.nikmang.shserver.game.GameState#SPECIAL} for it to succeed.
 * Must supply user name of target user. Case insensitive.
 */
class InspectPartyCard extends Command {

    private GameController gameController;

    public InspectPartyCard(MessageController messageController, GameController gameController) {
        super(messageController);

        this.gameController = gameController;
    }

    @Override
    public synchronized void execute(ClientHandler clientHandler, String[] args) throws IOException {
        if(gameController.getGameState() != GameState.SPECIAL) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "Not in the correct game state currently. Try again.",
                    false);
            return;
        }

        if(!gameController.getPresident().equals(clientHandler.getUser())) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "You must be president to view others cards.",
                    false);
            return;
        }

        if(args.length < 1) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "You must supply target user name.",
                    false);
            return;
        }

        if(args[0].equalsIgnoreCase(clientHandler.getUser().getName())) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "Don't waste this on yourself...",
                    false);
            return;
        }

        Party p = gameController.inspectUserPartyCard(ClientHandler.getUserByName(args[0]));

        if(p == Party.NONE) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "User not found try again.",
                    false);
            return;
        }

        getMessageController().sendMessageAsServer(
                clientHandler.getUser(),
                String.format("%s's party card is: %s", args[0], p),
                false);

        getMessageController().broadcastAsServer(String.format(
                "%s has inspected %s's party card",
                clientHandler.getUser().getName(),
                args[0]));
    }
}
