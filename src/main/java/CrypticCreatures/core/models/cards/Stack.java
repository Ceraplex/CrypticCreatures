package CrypticCreatures.core.models.cards;

import java.util.List;
import lombok.Data;

@Data
public class Stack {
    //TODO: write neccessary Stack methods: addCards(), removeCard(), shuffle()
    public Stack () {

    }

    public void addCards(Stack stack){

    }

    public void addCard(Card card){
        cards.add(card);
    }


    private List<Card> cards;
}
