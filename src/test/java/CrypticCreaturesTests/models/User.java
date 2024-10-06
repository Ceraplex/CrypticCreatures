package CrypticCreaturesTests.models;

import CrypticCreatures.core.models.cards.Stack;

public class User {

    public Stack getStack() {
        return this.stack;
    }
    public Stack getDeck() {
        return this.deck;
    }
    public void setStack(Stack stack) {
        //TODO: user managed stack for fights
    }

    private Stack stack;
    private Stack deck;
    private int elo = 100;
}
