package CrypticCreatures.persistence.dao;

import CrypticCreatures.persistence.DbConnection;
import CrypticCreatures.core.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class UsersDaoDb implements Dao<User> {

    private final DbConnection dbConnection;

    public UsersDaoDb() {
        // Get the singleton instance of your custom DbConnection wrapper
        this.dbConnection = DbConnection.getInstance();
    }


    @Override
    public Optional<User> get(int id) {
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT fid, objectid, shape, anlname, bezirk, spielplatzdetail, typdetail, seannocaddata 
                FROM playgroundpoints 
                WHERE objectid=?
                """)
        ) {
            statement.setInt( 1, id );
            ResultSet resultSet = statement.executeQuery();
            if( resultSet.next() ) {
                return Optional.of( new User(
                        //get args from SQL select statement
                        /*
                        resultSet.getString(1),
                        resultSet.getInt( 2 ),
                        resultSet.getString( 3 ),
                        resultSet.getString( 4 ),
                        resultSet.getInt( 5 ),
                        resultSet.getString( 6 ),
                        resultSet.getString( 7 ),
                        resultSet.getString( 8 )
                        */
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> getAll() {
        ArrayList<User> result = new ArrayList<>();
        try ( PreparedStatement statement = DbConnection.getInstance().prepareStatement("""
                SELECT fid, objectid, shape, anlname, bezirk, spielplatzdetail, typdetail, seannocaddata 
                FROM playgroundpoints 
                """)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while( resultSet.next() ) {
                result.add( new User(
                        /*
                        resultSet.getString(1),
                        resultSet.getInt( 2 ),
                        resultSet.getString( 3 ),
                        resultSet.getString( 4 ),
                        resultSet.getInt( 5 ),
                        resultSet.getString( 6 ),
                        resultSet.getString( 7 ),
                        resultSet.getString( 8 )

                         */
                ) );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean save(User user) {
        try {
            // 1) Check if username already exists
            String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement preparedCheck = dbConnection.prepareStatement(sqlCheck)) {
                preparedCheck.setString(1, user.getUsername());
                try (ResultSet checkResult = preparedCheck.executeQuery()) {
                    if (checkResult.next() && checkResult.getInt(1) > 0) {
                        // Username is already in use
                        return false;
                    }
                }
            }

            // 2) Insert new user (include money, elo, and optionally profile_page_id)
            String sqlInsert =
                    "INSERT INTO users (username, password, money, elo, profile_page_id) " +
                            "VALUES (?, ?, ?, ?, ?)";

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
    @Override
    public boolean update(User playgroundPoint, String[] params) {
        return true;
    }

    //TODO: implement delete user
    @Override
    public boolean delete(User User) {
        return true;
    }

}
