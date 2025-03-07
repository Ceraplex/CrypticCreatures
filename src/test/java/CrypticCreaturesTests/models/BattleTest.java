package CrypticCreaturesTests.models;

import CrypticCreatures.services.Battle;
import CrypticCreatures.core.models.User;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.ElementType;
import CrypticCreatures.core.models.cards.CreatureType;
import CrypticCreatures.core.models.cards.monster.MonsterCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;


public class BattleTest {

    static class TestUser extends User {
        private final List<Card> fixedDeck;

        public TestUser(String username, List<Card> deck) {
            // Use a dummy password and default money/elo.
            super(username, "password", 100, 100);
            // Copy the provided deck so that it remains fixed.
            this.fixedDeck = new ArrayList<>(deck);
            // Initialize the deck.
            this.setDeck(new ArrayList<>(deck));
        }

        @Override
        public void setRandomDeck() {
            // Instead of randomizing, reset the deck to the fixed deck.
            this.getDeck().clear();
            this.getDeck().addAll(fixedDeck);
        }
    }

    private MonsterCard createMonsterCard(String cid, double dmg, ElementType element) {
        return new MonsterCard(cid, "Monster" + cid, dmg, element, CreatureType.DRAGON);
    }

    @Test
    void testFight_Player1Wins() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        // Create 4 MonsterCards for each player.
        // For player1, use damage 10; for player2, damage 5.
        for (int i = 0; i < 4; i++) {
            deck1.add(createMonsterCard("p1_" + i, 10, ElementType.FIRE));
            deck2.add(createMonsterCard("p2_" + i, 5, ElementType.FIRE));
        }
        TestUser player1 = new TestUser("Player1", deck1);
        TestUser player2 = new TestUser("Player2", deck2);

        Battle battle = new Battle();
        battle.fight(player1, player2);

        // In each round, player1 wins so eventually player2's deck should become empty.
        assertTrue(player2.getDeck().isEmpty(), "Player2's deck should be empty when player1 wins all rounds");
        assertFalse(player1.getDeck().isEmpty(), "Player1's deck should not be empty");
    }

    @Test
    void testFight_Tie() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        // Create 4 MonsterCards for each player with identical damage and element.
        for (int i = 0; i < 4; i++) {
            deck1.add(createMonsterCard("p1_" + i, 7, ElementType.WATER));
            deck2.add(createMonsterCard("p2_" + i, 7, ElementType.WATER));
        }
        TestUser player1 = new TestUser("Player1", deck1);
        TestUser player2 = new TestUser("Player2", deck2);

        Battle battle = new Battle();
        battle.fight(player1, player2);

        // Because every round is a tie, the decks should remain unchanged in count.
        assertEquals(4, player1.getDeck().size(), "Player1 should still have 4 cards after tie rounds");
        assertEquals(4, player2.getDeck().size(), "Player2 should still have 4 cards after tie rounds");
    }

    @Test
    void testFight_MaxRounds() {
        List<Card> deck1 = new ArrayList<>();
        List<Card> deck2 = new ArrayList<>();
        // Create 6 cards each; use identical cards so rounds tie and decks never shrink.
        for (int i = 0; i < 6; i++) {
            deck1.add(createMonsterCard("p1_" + i, 5, ElementType.EARTH));
            deck2.add(createMonsterCard("p2_" + i, 5, ElementType.EARTH));
        }
        TestUser player1 = new TestUser("Player1", deck1);
        TestUser player2 = new TestUser("Player2", deck2);

        Battle battle = new Battle();
        battle.fight(player1, player2);

        // After 10 rounds of tie, each player's deck should still have the original number of cards.
        assertEquals(6, player1.getDeck().size(), "Player1 should have 6 cards after 10 rounds of tie");
        assertEquals(6, player2.getDeck().size(), "Player2 should have 6 cards after 10 rounds of tie");
    }
}
