package CrypticCreatures.models;

import CrypticCreatures.models.card.Card;

import java.util.List;

public class Stack {
    public Stack () {

    }

    public void addCards(List<Card>newCards){
        this.Cards.addAll(newCards);
    }


    private List<Card> Cards;
}
