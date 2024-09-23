package CrypticCreaturesTests.models.cards;

import CrypticCreatures.models.cards.Card;

import java.util.List;

public class Stack {
    public Stack () {

    }

    public void addCards(List<CrypticCreatures.models.cards.Card>newCards){
        this.Cards.addAll(newCards);
    }


    private List<Card> Cards;
}
