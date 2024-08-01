package CrypticCreatures.service;

import CrypticCreatures.models.User;
import CrypticCreatures.models.cards.*;
import CrypticCreatures.models.cards.spell.SpellCard;


public class Battle {
    public static void fight(User player1, User player2) {

    }
    private static void round(Card card1, Card card2){
        if (card1 instanceof SpellCard || card2 instanceof SpellCard){
            //TODO: battle logic with elemental type
        } else {
            //TODO: battle logic without elemental type
        }
    }
    private static void battleLog(){};
}
