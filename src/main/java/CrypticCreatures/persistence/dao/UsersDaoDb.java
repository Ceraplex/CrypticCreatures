package CrypticCreatures.persistence.dao;

import CrypticCreatures.persistence.DbConnection;
import CrypticCreatures.core.models.User;

import java.sql.*;
import java.util.Collection;
import java.util.Optional;

public class UsersDaoDb {

    private final DbConnection dbConnection;

    public UsersDaoDb() {
        // Get the singleton instance of your custom DbConnection wrapper
        this.dbConnection = DbConnection.getInstance();
    }

    public Optional<User> get(int id) {
        try {
            CardDaoDb cardDaoDb = new CardDaoDb();
            try (PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM users WHERE uid = ?")) {
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
                        foundUser.setDeck(cardDaoDb.getAllByUser(foundUser));
                        return Optional.of(foundUser);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //TODO: implement get all users
    public Collection<User> getAll() {
        return null;
    }
    public Optional<User> getUserByUsername(String username) throws SQLException {
        try (PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            CardDaoDb cardDaoDb = new CardDaoDb();
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User foundUser = new User(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getInt("money"),
                            resultSet.getInt("elo")
                    );
                    foundUser.setUid(resultSet.getInt("uid"));
                    foundUser.setDeck(cardDaoDb.getAllByUser(foundUser));
                    return Optional.of(foundUser);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    public boolean save(User user) throws SQLException {
        try {
            // 1) Check if username already exists
            String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement preparedCheck = dbConnection.prepareStatement(sqlCheck)) {
                preparedCheck.setString(1, user.getUsername());
                try (ResultSet checkResult = preparedCheck.executeQuery()) {
                    if (checkResult.next() && checkResult.getInt(1) > 0) {
                        // Username already exists
                        return false;
                    }
                }
            }

            // 2) Insert new user (include money, elo, and optionally profile_page_id)
            String sqlInsert =
                    "INSERT INTO users (username, password, money, elo) " + "VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedInsert = dbConnection.prepareStatement(sqlInsert)) {
                preparedInsert.setString(1, user.getUsername());
                preparedInsert.setString(2, user.getPassword());
                preparedInsert.setInt(3, user.getMoney());  // Must be >= 0, per your schema
                preparedInsert.setInt(4, user.getElo());    // Must be >= 0, per your schema

                int rowsAffected = preparedInsert.executeUpdate();
            }
            String sqlGetId = "SELECT uid FROM users WHERE username = ?";
            try (PreparedStatement preparedGetId = dbConnection.prepareStatement(sqlGetId)) {
                preparedGetId.setString(1, user.getUsername());
                try (ResultSet idResult = preparedGetId.executeQuery()) {
                    if (idResult.next()) {
                        user.setUid(idResult.getInt(1));
                    }
                }
            }
            String sqlInitProfile = "INSERT INTO profile_pages (pid) VALUES (?)";
            try (PreparedStatement preparedInitProfile = dbConnection.prepareStatement(sqlInitProfile)) {
                preparedInitProfile.setInt(1, user.getUid());
                int rowsAffected= preparedInitProfile.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean update(User user) {
        try {
            try(PreparedStatement statement = dbConnection.prepareStatement("UPDATE users SET password = ?, money = ?, elo = ? WHERE username = ?")){
                statement.setString(1, user.getPassword());
                statement.setInt(2, user.getMoney());
                statement.setInt(3, user.getElo());
                statement.setString(4, user.getUsername());
                int affected = statement.executeUpdate();
                return affected > 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean delete(User user) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
