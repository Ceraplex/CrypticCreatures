package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.User;
import CrypticCreatures.persistence.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class BattleDaoDb {
    private final DbConnection dbConnection;

    public BattleDaoDb() {
        // Get the singleton instance of your custom DbConnection wrapper
        this.dbConnection = DbConnection.getInstance();
    }

    public boolean enterQueue(User user) {
        return false;
    }

    public Optional<User> getWaitingUserById(int id) {
        try{
            try (PreparedStatement statement = dbConnection.prepareStatement("SELECT uid FROM battle_queue WHERE uid = ?")){
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User foundUser = new User(
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getInt("money"),
                                resultSet.getInt("elo")
                        );
                        foundUser.setUid(resultSet.getInt("uid"));
                        return Optional.of(foundUser);
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional get(int id) {
        return Optional.empty();
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try{
            try (PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM battle_queue")){
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        User foundUser = new User(
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getInt("money"),
                                resultSet.getInt("elo")
                        );
                        foundUser.setUid(resultSet.getInt("uid"));
                        users.add(foundUser);
                    }
                    return users;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean save(User user) {
        try{
            try (PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO battle_queue (uid, elo) VALUES (?, ?)")){
                statement.setInt(1, user.getUid());
                statement.setInt(5, user.getElo());
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Object o) {
        return false;
    }

    public boolean delete(Object o) {
        return false;
    }
}
