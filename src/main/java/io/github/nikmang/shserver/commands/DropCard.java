package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.MessageController;
import io.github.nikmang.shserver.client.ClientHandler;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.game.GameState;

import java.io.IOException;

/**
 * Executed by president to drop a card out of the choice of <b>3</b> cards.
 *
 * Chancellor must have already been chosen for this to work.
 * Must supply argument (1, 2, 3) of card you want to remove.
 */
class DropCard extends Command {

    private GameController gameController;

    DropCard(MessageController messageController, GameController gameController) {
        super(messageController);

        this.gameController = gameController;
    }

    @Override
    public void execute(ClientHandler clientHandler, String[] args) throws IOException {
        if(!gameController.getPresident().equals(clientHandler.getUser())) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "You must be president to drop cards.",
                    false);

            return;
        }

        if(gameController.getGameState() != GameState.CARD_CHOICE) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "Wait until chancellor is set.",
                    false);

            return;
        }

        if(gameController.getCardsInPlay().size() != 3) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "Cards have already been dropped. Cannot drop cards again.",
                    false);

            return;
        }

        if(args.length < 1) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "Need to supply the index of the card you wish to drop.",
                    false);

            return;
        }

        try {
            int index = Integer.parseInt(args[0]);

            if(gameController.removeCardFromPlay(index-1)) {
                getMessageController().sendMessageAsServer(
                        clientHandler.getUser(),
                        String.format("You have removed the %d card from the deck.", index),
                        false);

                getMessageController().broadcastAsServer(String.format(
                        "%s has dropped a card. %s should now choose which of two to play.",
                        gameController.getPresident().getName(),
                        gameController.getChancellor().getName()));

                getMessageController().sendCards(
                        gameController.getChancellor(),
                        gameController.getCardsInPlay(),
                        "Please choose which one to play.");
            } else {
                getMessageController().sendMessageAsServer(
                        clientHandler.getUser(),
                        "Need to supply the index that's 1, 2 or 3.",
                        false);
            }
        } catch (NumberFormatException e) {
            getMessageController().sendMessageAsServer(
                    clientHandler.getUser(),
                    "Need to supply the index of the card you wish to drop.",
                    false);

            return;
        }
    }
}
