package CrypticCreatures.core.models;

import CrypticCreatures.core.models.cards.Stack;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;
    private Integer profilePageId;

    private String token;
    private Stack stack = new Stack();
    private Stack deck = new Stack();
    private int money = 0;
    private int elo = 100;


    // Custom constructor for initializing fields from the database
    public User(String username, String password, int money, int elo, Integer profilePageId) {
        this.username = username;
        this.password = password;
        this.money = money;
        this.elo = elo;
        this.profilePageId = profilePageId;
    }

    public static void setStack(Stack stack) {
        //TODO: user managed stack for fights
    }

    public void setUsername(String username) {
        this.username = username;
        this.token = username + "-mtcgToken";
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


}
