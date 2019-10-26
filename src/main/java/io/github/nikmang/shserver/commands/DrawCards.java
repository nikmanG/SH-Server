package io.github.nikmang.shserver.commands;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.controllers.GameController;
import io.github.nikmang.shserver.controllers.MessageController;
import io.github.nikmang.shserver.game.GameDeck;
import io.github.nikmang.shserver.handlers.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrawCards extends Command {


    private GameController gameController;

    public DrawCards(MessageController messageController, GameController gameController) {
        super(messageController);

        this.gameController = gameController;
    }

    @Override
    public void execute(ClientHandler handler, String[] args) throws IOException {
        User user = handler.getUser();

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

        List<GameDeck.Card> drawnCards = gameController.getCardsInPlay();

        this.getMessageController().sendCards(user, drawnCards, null);
    }
}
