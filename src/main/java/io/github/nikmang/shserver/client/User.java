package io.github.nikmang.shserver.client;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Output info for client connection.
 */
public class User {
    private String name;
    private DataOutputStream output;
    private Party politicalParty;

    public User(String name, DataOutputStream output) {
        this.name = name;
        this.output = output;
        this.politicalParty = Party.NONE;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns {@link Party} of user.<br>
     * If user is {@link Party#HITLER} then {@link Party#FASCIST} is returned.<br>
     * To check if player is hitler, call {@linkplain #isHitler()}.
     *
     * @return Political party of user.
     */
    public Party getPoliticalParty() {
        if(politicalParty == Party.HITLER)
            return Party.FASCIST;

        return politicalParty;
    }

    /**
     * Checks if player is hitler. Used because {@linkplain #getPoliticalParty()} will <b>not</b> return if player is
     * Hitler or not, but only if they are a fascist or liberal.
     *
     * @return <b>true</b> if player has the political party of {@link Party#HITLER}.
     */
    public boolean isHitler() {
        return politicalParty == Party.HITLER;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Send a JSON message to the user.
     *
     * @param packet JSON packet to be sent to the user.
     * @throws IOException If outstream is closed.
     */
    public void sendMessage(JsonPacketBuilder packet) throws IOException {
        output.writeUTF(packet.build());
    }

    public void setPoliticalParty(Party party) {
        this.politicalParty = party;
    }

    public void close() throws IOException {
        output.close();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof User))
            return false;

        return this.name.equalsIgnoreCase(((User) o).getName());
    }

    @Override
    public int hashCode() {
        return 37 * this.name.hashCode();
    }
}
