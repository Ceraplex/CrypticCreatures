package CrypticCreatures.core.models.cards;

import CrypticCreatures.core.models.cards.spell.SpellCard;
import CrypticCreatures.core.models.cards.monster.MonsterCard;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.lang.Math;

@Data
public abstract class Card {
    @JsonProperty("Id")
    protected String cid;
    @JsonProperty("Name")
    protected String name;
    @JsonProperty("Damage")
    protected double dmg;
    protected ElementType elementType;

    public Card(String cid, String name, double damage, ElementType elementType) {
        this.cid = cid;
        this.name = name;
        this.dmg = damage;
        this.elementType = elementType;
    }

    public void printCard(){
        //TODO: debug print of cards
        System.out.println("Card Name: " + this.name);
        System.out.println("Damage: " + this.dmg);
        System.out.println("Element Type: " + this.elementType);
    };

    public static Card createRandomCard(String cid, String name, double damage){
        if(Math.random() < 0.5){
            return new SpellCard(cid, name, damage, getRandomElementType());
        } else {
            return new MonsterCard(cid, name, damage, getRandomElementType(), Card.getRandomCreatureType());
        }
    }

    public void initializeRandomCard(){
        if(Math.random() < 0.5){
            this.elementType = getRandomElementType();
        } else {
            this.elementType = getRandomElementType();
        }
    }

    private static ElementType getRandomElementType(){
        //Earth is slightly more common, Energy is rare
        if(Math.random() < 0.2){
            return ElementType.FIRE;
        } else if(Math.random() >= 0.2 && Math.random() < 0.4){
            return ElementType.WATER;
        } else if(Math.random() >= 0.4 && Math.random() < 0.7){
            return ElementType.EARTH;
        } else if(Math.random() >= 0.7 && Math.random() < 0.9){
            return ElementType.AIR;
        } else {
            return ElementType.ENERGY;
        }
    }

    private static CreatureType getRandomCreatureType(){
        //Creatures with higher damage multipliers are less common
        if(Math.random() < 0.05){
            return CreatureType.KRAKEN;
        } else if(Math.random() >= 0.05 && Math.random() < 0.13){
            return CreatureType.DRAGON;
        } else if(Math.random() >= 0.13 && Math.random() < 0.21){
            return CreatureType.WIZARD;
        } else if(Math.random() >= 0.21 && Math.random() < 0.32){
            return CreatureType.ELF;
        } else if(Math.random() >= 0.32 && Math.random() < 0.47){
            return CreatureType.KNIGHTS;
        } else if(Math.random() >= 0.47 && Math.random() < 0.72){
            return CreatureType.ORK;
        } else {
            return CreatureType.GOBLIN;
        }

    }
}
