package CrypticCreaturesTests.models.cards.monster;

import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.monster.CreatureType;

public class MonsterCard extends Card {

    CreatureType creatureType;
    public MonsterCard() {

    }

    @Override
    public void printCard (){
        super.printCard();
        System.out.println("Creature Type: " + creatureType);
    }
}
