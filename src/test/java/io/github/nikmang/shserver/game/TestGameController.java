package io.github.nikmang.shserver.game;

import io.github.nikmang.shserver.client.User;
import io.github.nikmang.shserver.game.Card;
import io.github.nikmang.shserver.game.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestGameController {

    private GameController testGameController;

    @BeforeEach
    public void setup() {
        testGameController = new GameController();
    }

    @Test
    public void testGetCardsInPlayFresh() {
        //Given
        //When
        List<Card> cards = testGameController.getCardsInPlay();

        //Then
        assertEquals(3, cards.size());
    }

    @Test
    public void testGetCardsInPlayCache() {
        //Given
        //When
        List<Card> cards = testGameController.getCardsInPlay();
        List<Card> cards2 = testGameController.getCardsInPlay();

        //Then
        assertEquals(cards, cards2);
    }

    @Test
    public void testGetCardsInPlayUnmodifiable() {
        //Given
        //When
        List<Card> cards = testGameController.getCardsInPlay();

        //Then
        assertThrows(UnsupportedOperationException.class, () -> cards.remove(0));
    }

    @Test
    public void testNewDeckGetCardsInPlay() {
        //Given
        List<Card> cards = testGameController.getCardsInPlay();
        testGameController.removeCardFromPlay(2);
        testGameController.removeCardFromPlay(1);
        testGameController.removeCardFromPlay(0);

        //When
        List<Card> testCards = testGameController.getCardsInPlay();

        //Then
        assertNotSame(cards, testCards);
    }

    @Test
    public void testRemoveCardsInvalid() {
        //Given
        //When
        boolean b1 = testGameController.removeCardFromPlay(5);
        boolean b2 = testGameController.removeCardFromPlay(-1);

        //Then
        assertFalse(b1);
        assertFalse(b2);
    }

    @Test
    public void testRemoveCardValid() {
        //Given
        List<Card> cards = new ArrayList<>(testGameController.getCardsInPlay());

        //When
        testGameController.removeCardFromPlay(1);

        //Then
        assertEquals(Arrays.asList(cards.get(0), cards.get(2)), testGameController.getCardsInPlay());
    }

    @Test
    public void addPresidentSuccessful() {
        //Given
        User user = new User("test-user", null);

        //When
        boolean b = testGameController.setPresident(user);

        //Then
        assertEquals(user, testGameController.getPresident());
        assertTrue(b);
    }

    @Test
    public void addPresidentSuccessfullOverride() {
        //Given
        User user = new User("test-user", null);
        User user2 = new User("test-user2", null);
        testGameController.setPresident(user);

        //When
        boolean b = testGameController.setPresident(user2);

        //Then
        assertEquals(user2, testGameController.getPresident());
        assertTrue(b);
    }

    @Test
    public void addPresidentAlreadyPresident() {
        //Given
        User user = new User("test-user", null);
        testGameController.setPresident(user);

        //When
        boolean b = testGameController.setPresident(user);

        //Then
        assertEquals(user, testGameController.getPresident());
        assertFalse(b);
    }

    @Test
    public void addPresidentAlreadyChancellor() {
        //Given
        User user = new User("test-user", null);
        testGameController.setChancellor(user);

        //When
        boolean b = testGameController.setPresident(user);

        //Then
        assertNull(testGameController.getPresident());
        assertEquals(user, testGameController.getChancellor());
        assertFalse(b);
    }

    @Test
    public void addChancellorSuccessful() {
        //Given
        User user = new User("test-user", null);

        //When
        boolean b = testGameController.setChancellor(user);

        //Then
        assertEquals(user, testGameController.getChancellor());
        assertTrue(b);
    }

    @Test
    public void addChancellorSuccessfullOverride() {
        //Given
        User user = new User("test-user", null);
        User user2 = new User("test-user2", null);
        testGameController.setChancellor(user);

        //When
        boolean b = testGameController.setChancellor(user2);

        //Then
        assertEquals(user2, testGameController.getChancellor());
        assertTrue(b);
    }

    @Test
    public void addChancellorAlreadyChancellor() {
        //Given
        User user = new User("test-user", null);
        testGameController.setChancellor(user);

        //When
        boolean b = testGameController.setChancellor(user);

        //Then
        assertEquals(user, testGameController.getChancellor());
        assertFalse(b);
    }

    @Test
    public void addChancellorAlreadyPresident() {
        //Given
        User user = new User("test-user", null);
        testGameController.setPresident(user);

        //When
        boolean b = testGameController.setChancellor(user);

        //Then
        assertNull(testGameController.getChancellor());
        assertEquals(user, testGameController.getPresident());
        assertFalse(b);
    }
}
