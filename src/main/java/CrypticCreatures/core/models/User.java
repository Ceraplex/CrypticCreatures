package CrypticCreatures.core.models;

import CrypticCreatures.core.models.cards.Card;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class User {

    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;
    private Integer profilePageId;
    private Integer uid;

    private List<Card> stack = new ArrayList<Card>();
    private List<Card> deck = new ArrayList<Card>();
    private int money = 20;
    private int elo = 100;


    // Custom constructor for initializing fields from the database
    public User(String username, String password, int money, int elo) {
        this.username = username;
        this.password = password;
        this.money = money;
        this.elo = elo;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof User)){
            return false;
        }

        User user = (User) obj;
        return user.getUsername().equals(this.getUsername());
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

    public String getToken() {
        return username + "-mtcgToken";
    }

    public void setRandomDeck(){
        // Return deck cards to stack
        if (!deck.isEmpty()) {
            stack.addAll(deck);
            deck.clear();
        }

        // Check if there are 4 cards available
        if (stack.size() < 4) {
            System.out.println("Not enough cards in the stack to build a deck.");
            return;
        }

        // Get 4 random cards from stack
        for (int i = 0; i < 4; i++) {
            int randomIndex = (int) (Math.random() * stack.size());
            Card selectedCard = stack.remove(randomIndex);
            deck.add(selectedCard);
        }
    }

    public void returnDeckToStack(){
        stack.addAll(deck);
        deck.clear();
    }

}
