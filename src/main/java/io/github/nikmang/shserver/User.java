package io.github.nikmang.shserver;

import io.github.nikmang.shserver.game.Party;

import java.io.DataOutputStream;
import java.io.IOException;

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

    public void setName(String name) {
        this.name = name;
    }

    public void sendMessage(JsonPacketBuilder packet) throws IOException {
        output.writeUTF(packet.build());
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
