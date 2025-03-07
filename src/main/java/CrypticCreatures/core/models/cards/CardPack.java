package CrypticCreatures.core.models.cards;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//Packages can be bought with coins
@Data
public class CardPack {
    private int pid;
    private List<Card> cards = new ArrayList<Card>();
    private int cost = 5;

    public void addCard(Card card){
        cards.add(card);
    }
}
