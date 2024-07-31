package CrypticCreatures.models;

import CrypticCreatures.models.Stack;

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
}
