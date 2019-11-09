package io.github.nikmang.shserver.game;

/**
 * Current state of the game.
 * <b>SPECIAL</b> is during fascist tile events e.g) president killing others.
 */
public enum GameState {

    LOBBY,
    END,
    VOTING,
    SPECIAL,
    CARD_CHOICE
}
