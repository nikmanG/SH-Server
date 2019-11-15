package io.github.nikmang.shserver.game;

import io.github.nikmang.shserver.client.ClientHandler;
import io.github.nikmang.shserver.client.Party;
import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.configurations.PlayerConfigurationFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class responsible for controlling the game.
 * This maintains individual gameflow (game state, game board, cards, leader roles).
 */
public class GameController {

    private GameDeck gameDeck;
    private GameBoard gameBoard;
    private GameState gameState;
    private GameBoardEffect currentEffect;

    private List<Card> cardsInPlay;

    private User president;
    private User chancellor;

    /**
     * Create a game controller instance with specified player count for game board onfiguration.
     *
     * @param playerCount Amount of players in the game.
     */
    public GameController(int playerCount){
        gameDeck = new GameDeck();
        gameBoard = new GameBoard(PlayerConfigurationFactory.getConfigurationOnPlayerCount(playerCount));
        gameState = GameState.LOBBY;
        currentEffect = GameBoardEffect.NONE;
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
     * Updates the game state to {@link GameState#CARD_CHOICE}.
     *
     * @param index Index of the card bust be within the list of provided cards (e.g 3 cards means can only choose 0,1,2).
     * @return <b>true</b> if index corresponded to a card and could be removed.
     */
    public boolean removeCardFromPlay(int index) {
        if(index >= cardsInPlay.size() || index < 0)
            return false;

        Card c = cardsInPlay.remove(index);

        gameDeck.addCardToDiscardPile(c);

        gameState = GameState.CARD_CHOICE;
        return true;
    }

    /**
     * Play a card and update active effect.
     * If successful, sets the game state to {@link GameState#SPECIAL}.
     * 
     * @param index Index of the card that is found in {@link #getCardsInPlay()}.
     * @return The {@link GameBoardEffect} for given play.
     */
    public GameBoardEffect playCard(int index) {
        if(index < cardsInPlay.size() && index > 0) {
            Card c = cardsInPlay.remove(index);

            currentEffect = gameBoard.playPiece(c);
            gameState = GameState.SPECIAL;
        }

        return currentEffect;
    }

    /**
     * Gets the political party of a given user.
     * Search for name using {@link ClientHandler#getUserByName(String)}.
     * Transitions game state to {@link GameState#VOTING}.
     *
     * @param user the target user for the action.
     * @return {@link Party} of target user. If no user found then {@link Party#NONE} is returned.
     */
    public Party inspectUserPartyCard(User user) {
        if(user != null) {
            gameState = GameState.VOTING;
            return user.getPoliticalParty();
        }

        return Party.NONE;
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

    public synchronized User getPresident() {
        return president;
    }

    public synchronized User getChancellor() {
        return chancellor;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
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
