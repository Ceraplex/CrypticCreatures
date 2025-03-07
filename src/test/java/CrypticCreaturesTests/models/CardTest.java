package CrypticCreaturesTests.models;

import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.ElementType;
import CrypticCreatures.core.models.cards.monster.MonsterCard;
import CrypticCreatures.core.models.cards.CreatureType;
import CrypticCreatures.core.models.cards.spell.SpellCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void testCreateRandomCard() {
        // Given
        String cid = "test1";
        String name = "Test Card";
        double damage = 10.0;

        // When: create a random card
        Card card = Card.createRandomCard(cid, name, damage);

        // Then
        assertNotNull(card, "The card should not be null");
        assertEquals(cid, card.getCid(), "Card id should match");
        assertEquals(name, card.getName(), "Card name should match");
        assertEquals(damage, card.getDmg(), "Card damage should match");
        assertNotNull(card.getElementType(), "Card element type should not be null");
        // Card should be either a SpellCard or MonsterCard
        assertTrue(card instanceof SpellCard || card instanceof MonsterCard, "Card must be either a SpellCard or MonsterCard");

        if (card instanceof MonsterCard) {
            MonsterCard monster = (MonsterCard) card;
            assertNotNull(monster.getCreatureType(), "MonsterCard should have a non-null creature type");
        }
    }

    @Test
    public void testSpellCardProperties() {
        // Given
        String cid = "spell1";
        String name = "Spell of Testing";
        double damage = 20.0;
        ElementType element = ElementType.FIRE;

        // When: Create a SpellCard instance
        SpellCard spellCard = new SpellCard(cid, name, damage, element);

        // Then: Validate properties
        assertEquals(cid, spellCard.getCid(), "SpellCard id should match");
        assertEquals(name, spellCard.getName(), "SpellCard name should match");
        assertEquals(damage, spellCard.getDmg(), "SpellCard damage should match");
        assertEquals(element, spellCard.getElementType(), "SpellCard element type should match");
    }

    @Test
    public void testMonsterCardProperties() {
        // Given
        String cid = "monster1";
        String name = "Monster of Testing";
        double damage = 30.0;
        ElementType element = ElementType.WATER;
        // We assume CreatureType is an enum with DRAGON as one of its values.
        CreatureType creature = CreatureType.DRAGON;

        // When: Create a MonsterCard instance
        MonsterCard monsterCard = new MonsterCard(cid, name, damage, element, creature);

        // Then: Validate properties
        assertEquals(cid, monsterCard.getCid(), "MonsterCard id should match");
        assertEquals(name, monsterCard.getName(), "MonsterCard name should match");
        assertEquals(damage, monsterCard.getDmg(), "MonsterCard damage should match");
        assertEquals(element, monsterCard.getElementType(), "MonsterCard element type should match");
        assertEquals(creature, monsterCard.getCreatureType(), "MonsterCard creature type should match");
    }
}

