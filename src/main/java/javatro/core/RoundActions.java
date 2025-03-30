package javatro.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Implements the game actions available in a round. */
public class RoundActions {
    /** The round these actions apply to. */
    private final Round round;

    /**
     * Creates a new set of actions for the given round.
     *
     * @param round The round to operate on
     */
    public RoundActions(Round round) {
        this.round = round;
    }

    /**
     * Plays a set of cards as a poker hand.
     *
     * @param cardIndices Indices of cards to play from the holding hand
     * @throws JavatroException If the play is invalid
     */
    public void playCards(List<Integer> cardIndices) throws JavatroException {
        assert cardIndices != null : "Card indices cannot be null";

        // Validation
        if (cardIndices.size() > Round.MAX_HAND_SIZE || cardIndices.isEmpty()) {
            throw JavatroException.invalidPlayedHand();
        }

        RoundState state = round.getState();
        if (state.getRemainingPlays() <= 0) {
            throw JavatroException.noPlaysRemaining();
        }

        assert !cardIndices.isEmpty() : "Card indices cannot be empty";
        assert cardIndices.size() <= Round.MAX_HAND_SIZE
                : "Cannot play more than " + Round.MAX_HAND_SIZE + " cards";
        assert state.getRemainingPlays() > 0 : "No plays remaining to execute this action";

        long oldScore = state.getCurrentScore();
        int oldRemainingPlays = state.getRemainingPlays();

        // Execute play
        List<Card> playedCards = round.playerHand.play(cardIndices);
        PokerHand result = HandResult.evaluateHand(playedCards);
        Score handScore = new Score();
        long pointsEarned = handScore.getScore(result, playedCards, round.playerJokers);
        state.addScore(pointsEarned);

        // Draw new cards to replace played ones
        round.playerHand.draw(cardIndices.size(), Round.deck);
        state.decrementPlays();

        assert state.getRemainingPlays() == oldRemainingPlays - 1
                : "Remaining plays should decrease by exactly 1";
        assert state.getCurrentScore() >= oldScore
                : "Score should not decrease after playing cards";
        assert round.playerHand.getHand().size() == Round.INITIAL_HAND_SIZE
                : "Hand size should be maintained after play";

        round.updateRoundVariables();
    }

    /**
     * Discards cards from the player's hand.
     *
     * @param cardIndices Indices of cards to discard from the holding hand
     * @throws JavatroException If the discard is invalid
     */
    public void discardCards(List<Integer> cardIndices) throws JavatroException {
        RoundState state = round.getState();
        Integer numberOfDiscards = cardIndices.size();

        assert cardIndices != null : "Card indices cannot be null";

        // Validation
        if (state.getRemainingDiscards() <= 0) {
            throw JavatroException.noRemainingDiscards();
        }

        if (numberOfDiscards > round.playerHand.getHand().size()
                || numberOfDiscards > state.getRemainingDiscards()) {
            throw JavatroException.tooManyDiscards();
        }

        if (cardIndices.size() < 1) {
            throw JavatroException.cannotDiscardZeroCards();
        }

        assert !cardIndices.isEmpty() : "Cannot discard zero cards";
        assert state.getRemainingDiscards() > 0 : "No discards remaining to execute this action";

        // Handle duplicates by using a Set
        Set<Integer> indicesToDiscard = new HashSet<>(cardIndices);
        int handSizeBefore = round.playerHand.getHand().size();
        int oldRemainingDiscards = state.getRemainingDiscards();

        // Execute discard
        round.playerHand.discard(cardIndices);
        state.decrementDiscards();
        round.playerHand.draw(indicesToDiscard.size(), Round.deck);

        assert state.getRemainingDiscards() == oldRemainingDiscards - 1
                : "Remaining discards should decrease by exactly 1";
        assert round.playerHand.getHand().size() == handSizeBefore
                : "Hand size should be maintained after discard";

        round.updateRoundVariables();
    }
}
