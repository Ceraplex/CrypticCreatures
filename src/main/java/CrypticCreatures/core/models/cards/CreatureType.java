package CrypticCreatures.core.models.cards;

public enum CreatureType {
    KRAKEN (5.0),
    DRAGON (4.0),
    WIZARD (3.5),
    ELF (3.0),
    KNIGHTS (2.5),
    ORK (1.2),
    GOBLIN (0.5);

    private double dmgMultiplier;
    CreatureType(double dmgMultiplier) {

    }
}
