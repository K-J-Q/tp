package javatro.core;

import static javatro.core.Card.Rank.ACE;
import static javatro.core.Card.Rank.FOUR;
import static javatro.core.Card.Rank.THREE;
import static javatro.core.Card.Rank.TWO;
import static javatro.core.Card.Suit.SPADES;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains unit tests for the {@link HoldingHandTest} class. It tests the various
 * methods of drawing, discarding and adding cards to ensure the correct cards are displayed and
 * played hand is correct.
 */
public class HoldingHandTest {

    private static Deck deck;

    /** Initialize a new deck for each test. */
    @BeforeEach
    void init() {
        deck = new Deck();
    }

    /** Test that HoldingHand can add cards, draw and discard cards. */
    @Test
    void testHoldingHand() throws JavatroException {
        HoldingHand holdingHand = new HoldingHand();
        for (int i = 0; i < 8; i++) {
            holdingHand.add(deck.draw());
        }
        int cardsRemaining = deck.getRemainingCards();
        assertEquals(44, cardsRemaining);
        holdingHand.discard(List.of(1, 3, 5), deck);
        for (int i = 0; i < 3; i++) {
            holdingHand.add(deck.draw());
        }
        assertEquals(41, deck.getRemainingCards());
        List<Card> playedHand = holdingHand.play(List.of(1, 4, 5), deck);
        assertEquals(3, playedHand.size());
        for (int i = 0; i < 3; i++) {
            holdingHand.add(deck.draw());
        }
        assertEquals(38, deck.getRemainingCards());
    }

    /** Test that HoldingHand can return the hand held. */
    @Test
    void testGetHoldingHand() throws JavatroException {
        HoldingHand holdingHand = new HoldingHand();
        Card cardOne = new Card(ACE, SPADES);
        Card cardTwo = new Card(TWO, SPADES);
        Card cardThree = new Card(THREE, SPADES);
        Card cardFour = new Card(FOUR, SPADES);
        List<Card> cards =
                new ArrayList<Card>() {
                    {
                        add(cardOne);
                        add(cardTwo);
                        add(cardThree);
                    }
                };
        for (Card card : cards) {
            holdingHand.add(card);
        }
        List<Card> hand = holdingHand.getHand();
        assertArrayEquals(new List[] {cards}, new List[] {hand});
    }

    /***
     * Test that JavatroException is thrown when an illegal card selection is played.
     */
    @Test
    void testIllegalPlay() throws JavatroException {
        HoldingHand holdingHand = new HoldingHand();
        List<Integer> playedHand = new ArrayList<Integer>();
        playedHand.add(-1);
        for (int i = 0; i < 8; i++) {
            holdingHand.add(deck.draw());
        }

        try {
            holdingHand.play(playedHand, deck);
            fail("Should have thrown an exception for illegal card index");
        } catch (JavatroException e) {
            assertEquals("Invalid index in cards to be played: -1", e.getMessage());
        }

        playedHand.remove(0);
        playedHand.add(500);
        try {
            holdingHand.play(playedHand, deck);
            fail("Should have thrown an exception for illegal card index");
        } catch (JavatroException e) {
            assertEquals("Invalid index in cards to be played: 500", e.getMessage());
        }

        playedHand.remove(0);
        for (int i = 0; i < 8; i++) {
            playedHand.add(i);
        }
        try {
            holdingHand.play(playedHand, deck);
            fail("Should have thrown an exception for illegal card index");
        } catch (JavatroException e) {
            assertEquals("Number of cards played (8) exceeds maximum allowed. (5)", e.getMessage());
        }
    }

    /***
     * Test that JavatroException is thrown when an illegal card selection is discarded.
     */
    @Test
    void testIllegalDiscard() throws JavatroException {
        HoldingHand holdingHand = new HoldingHand();
        List<Integer> playedHand = new ArrayList<Integer>();
        playedHand.add(-1);
        for (int i = 0; i < 8; i++) {
            holdingHand.add(deck.draw());
        }

        try {
            holdingHand.discard(playedHand, deck);
            fail("Should have thrown an exception for illegal card index");
        } catch (JavatroException e) {
            assertEquals("Invalid index in cards to be discarded: -1", e.getMessage());
        }

        playedHand.remove(0);
        playedHand.add(500);
        try {
            holdingHand.discard(playedHand, deck);
            fail("Should have thrown an exception for illegal card index");
        } catch (JavatroException e) {
            assertEquals("Invalid index in cards to be discarded: 500", e.getMessage());
        }

        playedHand.remove(0);
        for (int i = 0; i < 8; i++) {
            playedHand.add(i);
        }
        try {
            holdingHand.discard(playedHand, deck);
            fail("Should have thrown an exception for illegal card index");
        } catch (JavatroException e) {
            assertEquals(
                    "Number of cards discarded (8) exceeds maximum allowed. (5)", e.getMessage());
        }
    }

    /***
     * Test that JavatroException is thrown when a card cannot be added properly.
     */
    @Test
    void testIllegalAdd() throws JavatroException {
        HoldingHand holdingHand = new HoldingHand();
        Card cardToAdd = new Card(ACE, SPADES);
        for (int i = 0; i < 8; i++) {
            holdingHand.add(deck.draw());
        }
        assertThrowsExactly(JavatroException.class, () -> holdingHand.add(cardToAdd));
    }
}
