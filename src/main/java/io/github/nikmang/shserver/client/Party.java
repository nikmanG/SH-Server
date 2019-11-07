package io.github.nikmang.shserver.client;

/**
 * Political party of the user.
 * <b>HITLER</b> is treated same as <b>FASCIST</b> for all purposes but <b>HITLER</b> round voting.
 * <b>NONE</b> should only be during lobby period and not for users while in game.
 */
public enum Party {

    NONE,
    LIBERAL,
    FASCIST,
    HITLER

}
