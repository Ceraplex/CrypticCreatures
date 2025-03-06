package CrypticCreatures.core.models.cards.spell;

import CrypticCreatures.core.models.cards.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpellCard extends Card {

    public SpellCard(String cid, String name, double damage, ElementType elementType) {
        super(cid, name, damage, elementType);
    }
}
