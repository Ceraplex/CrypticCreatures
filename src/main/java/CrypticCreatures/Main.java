package CrypticCreatures;

import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.monster.MonsterCard;
import CrypticCreatures.httpServer.*;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        System.out.println("Server started");
        server.start(10001);

    }
    public static void buildDeck(){

    }
    public static Card createCard(){
        Card testCard = new MonsterCard();
        return testCard;
    };

}