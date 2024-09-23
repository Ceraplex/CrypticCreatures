package CrypticCreaturesTests.models.cards;

public abstract class Card {
    protected String name;
    protected int dmg;
    protected int elementType;

    public void printCard(){
        //TODO: debug print of cards
        System.out.println("Card Name: " + this.name);
        System.out.println("Damage: " + this.dmg);
        System.out.println("Element Type: " + this.elementType);
    };
}
