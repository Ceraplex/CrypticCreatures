package CrypticCreatures.persistence;

//TODO: Replace Class with real Database
import CrypticCreatures.core.models.User;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private Map<String, User> users;

    public Database(){
        users = new HashMap<>();
    }

    public Boolean addUser(User user){
        if(users.containsKey(user.getUsername())){
            return false;
        }
        users.put(user.getUsername(), user);
        return true;
    }

    public Boolean findUser(User user){
        return users.containsKey(user.getUsername());
    }

    public Boolean verifyUser(User user){
        User existingUser = users.get(user.getUsername());

        //return whether User exist and PW matches
        return existingUser != null && user.getPassword().equals(existingUser.getPassword());
    }
}
