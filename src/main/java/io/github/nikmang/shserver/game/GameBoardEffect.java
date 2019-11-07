package io.github.nikmang.shserver.game;

/**
 * Effect that is executed whenever {@link GameBoard#playPiece(Card)} is done.
 */
public enum GameBoardEffect {

    NONE,
    CHOOSE_NEXT_PRESIDENT,
    KILL,
    SEE_PARTY_CARD,
    SEE_TOP_CARDS;
}
