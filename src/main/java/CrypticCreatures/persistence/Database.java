package CrypticCreatures.persistence;

import CrypticCreatures.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> users;

    public Database(){
        users = new ArrayList<User>();
    }

    public Boolean addUser(User user){
        if(users.contains(user)){
            return false;
        }
        users.add(user);
        return true;
    }

    public Boolean findUser(User user){
        if(users.contains(user)){
            return true;
        } else {
            return false;
        }
    }
}
