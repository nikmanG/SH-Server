package io.github.nikmang.shserver.game;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.Card;
import io.github.nikmang.shserver.game.GameDeck;
import io.github.nikmang.shserver.game.GameState;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class responsible for controlling the game.
 * This maintains individual gameflow (game state, cards, leader roles).
 */
public class GameController {

    private GameDeck gameDeck;
    private GameState gameState;

    private List<Card> cardsInPlay;

    private User president;
    private User chancellor;

    {
        gameDeck = new GameDeck();
        gameState = GameState.LOBBY;
        cardsInPlay = Collections.emptyList();
    }

    /**
     * Gets the cards that are currently in play.
     * If no cards currently in play then returns 3 new cards from the deck.
     *
     * @return unmodifiable list of current cards in play.
     */
    public List<Card> getCardsInPlay() {
        if(cardsInPlay.isEmpty()) {
            cardsInPlay = gameDeck.getCards(3);
        }

        return Collections.unmodifiableList(cardsInPlay);
    }

    /**
     * Remove the card at specified index from the game, into the discarded pile.
     *
     * @param index Index of the card bust be within the list of provided cards (e.g 3 cards means can only choose 0,1,2).
     * @return <b>true</b> if index corresponded to a card and could be removed.
     */
    public boolean removeCardFromPlay(int index) {
        if(index >= cardsInPlay.size() || index < 0)
            return false;

        Card c = cardsInPlay.remove(index);

        gameDeck.addCardToDiscardPile(c);

        return true;
    }

    /**
     * Sets the president for the turn.
     *
     * @param user User to be set as president.
     * @return <b>true</b> if president was set (they were not currently chancellor or president).
     */
    public synchronized boolean setPresident(User user) {
        return setPositionOfPower(user, (u) -> president = u);
    }

    /**
     * Sets the chancellor for the turn.
     *
     * @param user User to be set as chancellor.
     * @return <b>true</b> if chancellor was set (they were not currently chancellor or president).
     */
    public synchronized boolean setChancellor(User user) {
        return setPositionOfPower(user, (u) -> chancellor = u);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public synchronized User getPresident() {
        return president;
    }

    public synchronized User getChancellor() {
        return chancellor;
    }

    private boolean setPositionOfPower(User user, Consumer<User> action) {
        if(president != null) {
            if(user.equals(president))
                return false;
        }

        if(chancellor != null) {
            if(chancellor.equals(user))
                return false;
        }

        action.accept(user);
        return true;
    }
}
