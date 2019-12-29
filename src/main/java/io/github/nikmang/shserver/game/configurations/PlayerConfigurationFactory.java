package io.github.nikmang.shserver.game.configurations;

import io.github.nikmang.shserver.game.GameBoardEffect;

/**
 * Static factory class to get game configuration based on player count.
 */
public class PlayerConfigurationFactory {

    private PlayerConfigurationFactory() {
    }

    /**
     * Gets the player configuration based on player count.
     *
     * @param playerCount Amount of players in game.
     * @return {@link PlayerConfiguration} depending on player count.
     */
    public static PlayerConfiguration getConfigurationOnPlayerCount(int playerCount) {
        PlayerConfiguration config;

        if (playerCount <= 6) {
            config = new PlayerConfiguration(
                    true,
                    playerCount - 2,
                    2);

            config.addGameEffect(3, GameBoardEffect.SEE_TOP_CARDS);
        } else if (playerCount <= 8) {
            config = new PlayerConfiguration(
                    false,
                    playerCount - 3,
                    3);

            config.addGameEffect(2, GameBoardEffect.SEE_PARTY_CARD);
            config.addGameEffect(3, GameBoardEffect.CHOOSE_NEXT_PRESIDENT);
        } else {
            config = new PlayerConfiguration(
                    false,
                    playerCount - 4,
                    4);

            config.addGameEffect(1, GameBoardEffect.SEE_PARTY_CARD);
            config.addGameEffect(2, GameBoardEffect.SEE_PARTY_CARD);
            config.addGameEffect(3, GameBoardEffect.CHOOSE_NEXT_PRESIDENT);
        }

        return config;
    }
}
