package CrypticCreatures.core.models.cards;

import lombok.Data;

import java.util.List;

//Packages can be bought with coins
@Data
public class CardPack {
    private int pid;
    private List<Card> cards;
    private int cost = 12;

    public void addCard(Card card){
        cards.add(card);
    }
}
