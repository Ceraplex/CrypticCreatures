package CrypticCreaturesTests.models.cards.monster;

import CrypticCreatures.models.cards.*;
import CrypticCreatures.models.cards.monster.CreatureType;

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
