package CrypticCreatures.core.models;

import CrypticCreatures.core.models.cards.Stack;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    public User(String Username, String Password) {
        this.Username = Username;
        this.Password = Password;
    }

    public static void setStack(Stack stack) {
        //TODO: user managed stack for fights
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
    private String Username;
    @JsonProperty("Password")
    private String Password;

    private final Stack stack = new Stack();
    private final Stack deck = new Stack();
    private int elo = 100;
}
