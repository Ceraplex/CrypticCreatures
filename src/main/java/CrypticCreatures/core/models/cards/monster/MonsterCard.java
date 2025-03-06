package CrypticCreatures.core.models.cards.monster;

import CrypticCreatures.core.models.cards.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonsterCard extends Card {

    private final CreatureType creatureType;

    public MonsterCard(String cid, String name, double damage, ElementType elementType, CreatureType creatureType) {
        super(cid, name, damage, elementType);
        this.creatureType = creatureType;
    }

    @Override
    public void printCard (){
        super.printCard();
        System.out.println("Creature Type: " + creatureType);
    }
}
