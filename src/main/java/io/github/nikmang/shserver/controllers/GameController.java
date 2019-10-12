package io.github.nikmang.shserver.controllers;

import io.github.nikmang.shserver.User;
import io.github.nikmang.shserver.game.GameDeck;
import io.github.nikmang.shserver.game.GameState;

import java.util.Collections;
import java.util.List;


public class GameController {

    private GameDeck gameDeck;
    private GameState gameState;

    private List<GameDeck.Card> cardsInPlay;

    private User president;
    private User chancellor;

    {
        gameDeck = new GameDeck();
        gameState = GameState.LOBBY;
        cardsInPlay = Collections.emptyList();
    }

    public List<GameDeck.Card> getCardsInPlay() {
        if(cardsInPlay.isEmpty()) {
            cardsInPlay = gameDeck.getCards(3);
        }

        //This should be immutable
        return cardsInPlay;
    }

    public boolean removeCardFromPlay(int index) {
        if(index >= cardsInPlay.size())
            return false;

        GameDeck.Card c = cardsInPlay.remove(index);

        gameDeck.addCardToDiscardPile(c);

        return true;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public User getPresident() {
        return president;
    }

    public void setPresident(User president) {
        this.president = president;
    }

    public User getChancellor() {
        return chancellor;
    }

    public void setChancellor(User chancellor) {
        this.chancellor = chancellor;
    }
}
