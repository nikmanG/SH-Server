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

    //This may need to be improved. If I never actually use the print method in Card, that is
    private String printInLine(List<GameDeck.Card> cards) {
        assert cards.size() > 0;

        List<String[]> splitCards = cards.stream().map(c -> c.toCardPrint().split("\n")).collect(Collectors.toList());
        List<String> recompiled = new ArrayList<>();

        int strLen = splitCards.get(0)[1].length();

        for (int i = 0; i < splitCards.get(0).length; i++) {
            final int index = i;

            recompiled.add(splitCards.stream().map(c -> c[index]).collect(Collectors.joining("\t")));
        }

        //add numbers to the bottom of card
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= splitCards.size(); i++) {
            String sidePadding = "%1$" + (Math.max(0, (strLen - 3) / 2.)) + "s";

            sb.append(sidePadding).append("[").append(i).append("]").append(sidePadding).append("\t");
        }

        recompiled.add(String.format(sb.toString(), ""));

        return String.join("\n", recompiled);
    }
}
