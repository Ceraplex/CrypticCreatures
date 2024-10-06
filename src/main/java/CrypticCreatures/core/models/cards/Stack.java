package CrypticCreatures.core.models.cards;

import java.util.List;

public class Stack {
    public Stack () {

    }

    public void addCards(List<Card>newCards){
        this.Cards.addAll(newCards);
    }


    private List<Card> Cards;
}
