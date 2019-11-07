package io.github.nikmang.shserver.game;

import io.github.nikmang.shserver.client.Party;
import io.github.nikmang.shserver.game.configurations.PlayerConfiguration;

public class GameBoard {

    private transient final int targetLiberal;
    private transient final int targetFascist;

    private transient PlayerConfiguration config;

    private int liberalCount;
    private int fascistCount;

    private int deadlock;

    /**
     * Creates default game size: 6 fascist spaces, 5 liberal spaces and no cards played.
     *
     * @param config Player configuration, should be based on player count.
     */
    public GameBoard(PlayerConfiguration config){
        this.config = config;

        targetFascist = 6;
        targetLiberal = 5;

        liberalCount = 0;
        fascistCount = 0;

        deadlock = 0;
    }

    /**
     * Places a card on to the field and returns said effect from the configuration it was played at.
     * Increments internal counter for said piece. Sets deadlock to 0.
     *
     * @param card Card type to be played.
     * @return game board effect that is specified in {@link PlayerConfiguration}.
     */
    public GameBoardEffect playPiece(Card card) {
        deadlock = 0;

        if(card == Card.LIBERAL) {
            liberalCount++;
            return GameBoardEffect.NONE;
        } else {
            fascistCount++;
            return config.getPieceEffect(fascistCount);
        }
    }

    /**
     * Get the winner for the game.
     * Based on if current card played count is equal to the target count.
     *
     * For statistical purposes {@link Party#HITLER} is also a winner if {@link Party#FASCIST} wins.
     *
     * Otherwise {@link Party#NONE} is returned.
     *
     * @return Party of the winner of the game
     */
    public Party getWinner() {
        if(fascistCount == targetFascist)
            return Party.FASCIST;

        if(liberalCount == targetLiberal)
            return Party.LIBERAL;

        return Party.NONE;
    }

    /**
     * Returns the current count for deadlocks in the game.
     *
     * @return current deadlock count.
     */
    public int getDeadlockCount() {
        return deadlock;
    }

    /**
     * Increments deadlock count.
     */
    public void incrementDeadlock() {
        deadlock++;
    }
}
