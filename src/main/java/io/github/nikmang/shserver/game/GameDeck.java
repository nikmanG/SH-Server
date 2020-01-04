package io.github.nikmang.shserver.game;

import java.util.*;

/**
 * Deck for each game.
 * Actions affect internal deque so for full reset a new GameDeck must be generated.
 */
public class GameDeck {

    private int liberalCards;
    private int fascistCards;

    private Deque<Card> cardDeck;
    private List<Card> discardPile;

    /**
     * Initializes GameDeck.
     * Defaults liberal cards to 6, and fascist cards to 11.
     */
    public GameDeck() {
        this.liberalCards = 6;
        this.fascistCards = 11;

        cardDeck = new ArrayDeque<>();
        discardPile = new ArrayList<>();

        generateDeck();
    }

    /**
     * Gets an individual card from top of the deck.
     * If no cards in deck, deck is regenerated.
     * <p>
     * The card is polled from the deck hence to return it, you will need to call {@link #addCardToDiscardPile(Card)}.
     *
     * @return Card from top of the deck.
     */
    public Card getCard() {
        //TODO: this may become a private method
        Card card = cardDeck.pollFirst();

        if (cardDeck.size() < 3)
            generateDeck();

        return card;
    }

    /**
     * Retrieve specified amount of cards.
     * If amount requested is less than remaining deck size, deck is regenerated.
     * <p>
     * The card is polled from the deck hence to return it, you will need to call {@link #addCardToDiscardPile(Card)}.
     *
     * @param amount Amount of cards requested to be retrieved.
     * @return List of cards in order that they appear on top of the deck.
     */
    public List<Card> getCards(int amount) {
        if (amount > cardDeck.size())
            generateDeck();

        List<Card> cards = new LinkedList<>();

        int deckSize = cardDeck.size();

        for (int i = 0; i < Math.min(amount, deckSize); i++) {
            cards.add(getCard());
        }

        return cards;
    }

    /**
     * Adds a card to the discard pile
     *
     * @param card Card to be added to the discard pile
     */
    public void addCardToDiscardPile(Card card) {
        discardPile.add(card);
    }

    private void generateDeck() {
        //TODO: heck to make sure that during the game no funny business occurs and 17 cards max remain

        List<Card> tempList = new ArrayList<>();

        // This should only occur once per game, but could throw it into a separate method if need be
        if (discardPile.isEmpty() && cardDeck.isEmpty()) {
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
}
