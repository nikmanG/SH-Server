package io.github.nikmang.shserver.game;

import java.util.*;

public class GameDeck {

    private int liberalCards;
    private int fascistCards;

    private Deque<Card> cardDeck;
    private List<Card> discardPile;

    public GameDeck() {
        this.liberalCards = 6;
        this.fascistCards = 11;

        cardDeck = new ArrayDeque<>();
        discardPile = new ArrayList<>();

        generateDeck();
    }

    public Card getCard() {
        Card card = cardDeck.pollFirst();

        if(cardDeck.size() < 3)
            generateDeck();

        return card;
    }

    public List<Card> getCards(int amount) {
        if(amount > cardDeck.size())
            generateDeck();

        List<Card> cards = new LinkedList<>();

        for(int i=0; i<Math.min(amount, cardDeck.size()); i++) {
            cards.add(getCard());
        }

        return cards;
    }

    public synchronized void addCardToDiscardPile(Card card) {
        discardPile.add(card);
    }

    private synchronized void generateDeck() {
        List<Card> tempList = new ArrayList<>();

        // This should only occur once per game, but could throw it into a separate method if need be
        if(discardPile.isEmpty() && cardDeck.isEmpty()) {
            for (int i = 0; i < liberalCards; i++) {
                tempList.add(Card.LIBERAL);
            }

            for (int i = 0; i < fascistCards; i++) {
                tempList.add(Card.FASCIST);
            }
        } else {
            tempList.addAll(cardDeck);
            tempList.addAll(discardPile);
            discardPile.clear();
        }

        Collections.shuffle(tempList);

        cardDeck.clear();
        cardDeck.addAll(tempList);
    }

    public enum Card {
        FASCIST,
        LIBERAL;

        public String toCardPrint() {
            char firstLetter = this.toString().charAt(0);

            String row1 = " ---------\n";
            String row2 = "|" + firstLetter + "        |\n";
            String row3 = "|         |\n";
            String row4 = "| " + this.toString() + " |\n";
            String row5 = "|         |\n";
            String row6 = "|        " + firstLetter + "|\n";
            String row7 = " ---------";

            return row1 + row2 + row3 + row4 + row5 + row6 + row7;
        }
    }
}
