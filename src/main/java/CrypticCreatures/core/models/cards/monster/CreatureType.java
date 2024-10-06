package CrypticCreatures.core.models.cards.monster;

public enum CreatureType {
    GOBLIN (0.5),
    DRAGON (4.0),
    ELF (3.0),
    WIZARD (3.5),
    ORK (1.2),
    KRAKEN (5.0),
    KNIGHTS (2.5);

    private double dmgMultiplier;
    CreatureType(double dmgMultiplier) {

    }
}
