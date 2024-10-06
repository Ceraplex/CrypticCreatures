package CrypticCreaturesTests.models.cards;

import CrypticCreatures.core.models.cards.Card;

import java.util.List;

public class Stack {
    public Stack () {

    }

    public void addCards(List<Card>newCards){
        this.Cards.addAll(newCards);
    }


    private List<Card> Cards;
}
