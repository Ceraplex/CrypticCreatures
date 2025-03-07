package CrypticCreatures.services;

import CrypticCreatures.core.models.User;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.ElementType;
import CrypticCreatures.core.models.cards.monster.MonsterCard;
import CrypticCreatures.persistence.dao.UsersDaoDb;


public class Battle {
    UsersDaoDb usersDaoDb = new UsersDaoDb();
    private StringBuilder battleLog = new StringBuilder();

    public void fight(User player1, User player2) {
        boolean battleRunning = true;
        int rounds = 0;
        player1.setRandomDeck();
        player2.setRandomDeck();
        battleLog.setLength(0); // delete old logs
        battleLog.append("Starting battle: ").append(player1).append(" vs ").append(player2).append("\n");

        System.out.println("Starting battle: " + player1 + " vs " + player2);
        System.out.println("Player 1 deck size: " + player1.getDeck().size());
        System.out.println("Player 2 deck size: " + player2.getDeck().size());

        if (player1.getDeck().isEmpty() || player2.getDeck().isEmpty()) {
            battleLog.append("One or both players have no valid deck.\n");
            System.out.println(battleLog.toString());
            return;
        }
        while (rounds < 100 && !player1.getDeck().isEmpty() && !player2.getDeck().isEmpty()) {
            Card card1 = player1.getDeck().remove(0);
            Card card2 = player2.getDeck().remove(0);

            battleLog.append("Round ").append(rounds + 1).append(": ").append(card1.getName()).append(" vs ").append(card2.getName()).append("\n");
            double result = round(card1, card2);

            if(result > 0){
                //Player1 win
                player1.getDeck().add(card1);
                player1.getDeck().add(card2);
                battleLog.append(player1).append(" wins the round.\n");
            } else if (result < 0) {
                //Player2 win
                player2.getDeck().add(card1);
                player2.getDeck().add(card2);
                battleLog.append(player2).append(" wins the round.\n");
            } else if (result == 0) {
                //Draw
                player1.getDeck().add(card1);
                player2.getDeck().add(card2);
                battleLog.append("Round is a draw.\n");
            }

            rounds++;

            battleLog.append("Battle over after ").append(rounds).append(" rounds.\n")
                    .append("Final Deck Sizes - ")
                    .append(player1).append(": ").append(player1.getDeck().size()).append(", ")
                    .append(player2).append(": ").append(player2.getDeck().size()).append("\n");

            System.out.println(battleLog.toString());
        }
        player1.returnDeckToStack();
        player2.returnDeckToStack();
        //save the lost and gained cards
        usersDaoDb.update(player1);
        usersDaoDb.update(player2);
    }

    private double round(Card card1, Card card2){
        double result = 0;
        if (card1 instanceof MonsterCard && card2 instanceof MonsterCard){
            //pure Creature fight
            result = card1.getDmg() - card2.getDmg();
        } else {
            //Apply Element advantage
            double card1Dmg = card1.getDmg() * getAdvantageMultiplier(card1.getElementType(), card2.getElementType());
            double card2Dmg = card2.getDmg() * getAdvantageMultiplier(card2.getElementType(), card1.getElementType());

            result = card1Dmg + card2Dmg;
        }

        return result;
    }
    private static void battleLog(){

    };

    private static double getAdvantageMultiplier(ElementType attacker, ElementType defender){
        if(attacker == defender){
            return 1;
        }
        switch(attacker){
            case FIRE:
                if (defender == ElementType.EARTH || defender == ElementType.AIR) {
                    return 1.5;
                } else if (defender == ElementType.WATER || defender == ElementType.ENERGY) {
                    return 0.5;
                }
                break;
            case WATER:
                if (defender == ElementType.FIRE || defender == ElementType.ENERGY) {
                    return 1.5;
                } else if (defender == ElementType.EARTH || defender == ElementType.AIR) {
                    return 0.5;
                }
                break;
            case ENERGY:
                if (defender == ElementType.FIRE || defender == ElementType.AIR) {
                    return 1.5;
                } else if (defender == ElementType.WATER || defender == ElementType.EARTH) {
                    return 0.5;
                }
                break;
            case EARTH:
                if (defender == ElementType.WATER || defender == ElementType.ENERGY) {
                    return 1.5;
                } else if (defender == ElementType.FIRE || defender == ElementType.AIR) {
                    return 0.5;
                }
                break;
            case AIR:
                if (defender == ElementType.WATER || defender == ElementType.EARTH) {
                    return 1.5;
                } else if (defender == ElementType.FIRE || defender == ElementType.ENERGY) {
                    return 0.5;
                }
                break;
        }
        return 1;
    }
}
