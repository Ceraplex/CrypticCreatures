package CrypticCreatures.core.models;

import CrypticCreatures.core.models.cards.Stack;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

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

    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    private String token;
    private final Stack stack = new Stack();
    private final Stack deck = new Stack();
    private int elo = 100;
}
