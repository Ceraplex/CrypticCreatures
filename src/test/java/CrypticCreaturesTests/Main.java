package CrypticCreaturesTests;

import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.monster.MonsterCard;

public class Main {
    public static void main(String[] args) {
        buildDeck();

    }
    public static void buildDeck(){

    }
    public static Card createCard(){
        Card testCard = new MonsterCard();
        return testCard;
    };

}