package io.github.nikmang.shserver.game.configurations;

import io.github.nikmang.shserver.game.GameBoardEffect;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for player count types
 */
public class PlayerConfiguration {

    private boolean hitlerKnowsFascists;
    private int liberalCount;
    private int fascistCount;

    private Map<Integer, GameBoardEffect> placeEffects;

    /**
     * Create PlayerConfiguration instance.
     * By default has spaces 4 and 5 on fascist board set to {@link GameBoardEffect#KILL}.
     *
     * @param hitlerKnowsFascists If hitler knows who the fascists are in the game.
     * @param liberalCount Amount of liberals that are in the game.
     * @param fascistCount Amount of fascists that are in the game.
     */
    public PlayerConfiguration(
            boolean hitlerKnowsFascists,
            int liberalCount,
            int fascistCount) {
        this.hitlerKnowsFascists = hitlerKnowsFascists;
        this.liberalCount = liberalCount;
        this.fascistCount = fascistCount;

        placeEffects = new HashMap<>();

        placeEffects.put(4, GameBoardEffect.KILL);
        placeEffects.put(5, GameBoardEffect.KILL);
    }

    /**
     * Get the effect for play the xth piece.
     * If nothing happens then {@link GameBoardEffect#NONE} is returned.
     *
     * @param location Number of tile that is played.
     * @return {@link GameBoardEffect} that has to take effect at that spot
     */
    public GameBoardEffect getPieceEffect(int location) {
        return placeEffects.getOrDefault(location, GameBoardEffect.NONE);
    }

    /**
     * Add game effect for placing card on the xth location.
     *
     * @param location Where the effect will take place.
     * @param effect What the effect will be.
     *
     * @return <b>false</b> if location already has an effect. <b>true</b> if effect was added.
     */
    public boolean addGameEffect(int location, GameBoardEffect effect) {
        if(placeEffects.containsKey(location))
            return false;

        placeEffects.put(location, effect);
        return true;
    }

    public boolean hitlerKnowsFascists() {
        return hitlerKnowsFascists;
    }

    public int getLiberalCount() {
        return liberalCount;
    }

    public int getFascistCount() {
        return fascistCount;
    }
}
