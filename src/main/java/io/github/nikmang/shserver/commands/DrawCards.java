package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.GameController;
import io.github.nikmang.shserver.messaging.MessageController;
import io.github.nikmang.shserver.game.Card;

import java.io.IOException;
import java.util.List;

/**
 * Called on /draw to draw cards.
 * Performs president and chancellor checks.
 */
class DrawCards extends Command {


    private GameController gameController;

    DrawCards(MessageController messageController, GameController gameController) {
        super(messageController);

        this.gameController = gameController;
    }

    @Override
    public void execute(User user, String[] args) throws IOException {
        if (gameController.getPresident() == null || gameController.getChancellor() == null) {
            getMessageController().sendMessageAsServer(
                    user,
                    "Need to wait for president and chancellor to be set before drawing",
                    false);


            return;
        }

//        TODO: chancellor check too dependent on current round state
        if (!gameController.getPresident().equals(user)) {
            getMessageController().sendMessageAsServer(
                    user,
                    "You must be president or chancellor to view cards",
                    false);

            return;
        }

        List<Card> drawnCards = gameController.getCardsInPlay();

        this.getMessageController().sendCards(user, drawnCards, null);
    }
}
