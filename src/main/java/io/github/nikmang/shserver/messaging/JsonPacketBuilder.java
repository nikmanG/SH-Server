package io.github.nikmang.shserver.messaging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.nikmang.shserver.game.Card;

import java.util.List;

/**
 * Builder for packets that are sent by {@link MessageController} to the clients.
 */
public class JsonPacketBuilder {
    private transient Gson gson;

    private String sender;
    private String message;
    private List<Card> cards;
    private List<String> users;

    public JsonPacketBuilder() {
        GsonBuilder jsonBuilder = new GsonBuilder().setPrettyPrinting();
        gson = jsonBuilder.create();
    }

    public JsonPacketBuilder withSender(String sender) {
        this.sender = sender;
        return this;
    }

    public JsonPacketBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public JsonPacketBuilder withFollowingCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    public JsonPacketBuilder withFollowingUsers(List<String> users) {
        this.users = users;
        return this;
    }

    /**
     * Returns JSON string of the values added to the object.
     *
     * @return JSON representation of the class.
     */
    public String build() {
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JsonPacketBuilder))
            return false;

        JsonPacketBuilder other = (JsonPacketBuilder) o;

        return this.build().equals(other.build());
    }
}
