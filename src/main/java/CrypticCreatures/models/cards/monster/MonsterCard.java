package CrypticCreatures.models.cards.monster;

import CrypticCreatures.models.cards.*;

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
