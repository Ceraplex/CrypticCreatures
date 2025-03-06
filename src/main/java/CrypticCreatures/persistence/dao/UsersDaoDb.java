package CrypticCreatures.persistence.dao;

import CrypticCreatures.persistence.DbConnection;
import CrypticCreatures.core.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class UsersDaoDb {

    private final DbConnection dbConnection;

    public UsersDaoDb() {
        // Get the singleton instance of your custom DbConnection wrapper
        this.dbConnection = DbConnection.getInstance();
    }

    //TODO: implement get user
    public Optional<User> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("")
        ) {

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    //TODO: implement get all users
    public Collection<User> getAll() {
        return null;
    }

    public boolean save(User user) {
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
                    "INSERT INTO users (username, password, money, elo, profile_page_id) " + "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedInsert = dbConnection.prepareStatement(sqlInsert)) {
                preparedInsert.setString(1, user.getUsername());
                preparedInsert.setString(2, user.getPassword());
                preparedInsert.setInt(3, user.getMoney());  // Must be >= 0, per your schema
                preparedInsert.setInt(4, user.getElo());    // Must be >= 0, per your schema

                // If the user has no associated profile_page_id, set it to null
                if (user.getProfilePageId() == null) {
                    preparedInsert.setNull(5, java.sql.Types.INTEGER);
                } else {
                    preparedInsert.setInt(5, user.getProfilePageId());
                }

                int rowsAffected = preparedInsert.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //TODO: implement update user
    public boolean update(User user, String[] params) {
        return true;
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

    public User getUserByUsername(String username) throws SQLException {
        try (PreparedStatement statement = dbConnection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getInt("money"),
                            resultSet.getInt("elo"),
                            resultSet.getInt("profile_page_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }
}
