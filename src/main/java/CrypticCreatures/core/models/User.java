package CrypticCreatures.core.models;

import CrypticCreatures.core.models.cards.Stack;
import lombok.Data;

@Data
public class User {

    public static void User(){

    }

    public static void setStack(Stack stack) {
        //TODO: user managed stack for fights
    }

    private String Username;
    private String Password;

    private final Stack stack;
    private final Stack deck;
    private int elo = 100;
}
