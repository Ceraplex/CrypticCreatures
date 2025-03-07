package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.User;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.core.models.cards.CardPack;
import CrypticCreatures.persistence.DbConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class StackDao {

    private final DbConnection dbConnection;

    public StackDao() {
        this.dbConnection = DbConnection.getInstance();
    }

    public boolean buyCardPack(User user, CardPack cardPack) {
        try{
            CardPackDaoDb cardPackDaoDb = new CardPackDaoDb();
            UsersDaoDb usersDaoDb = new UsersDaoDb();

            //check if card pack exists
            if(cardPackDaoDb.get(cardPack.getPid()).isEmpty()){
                return false;
            }
            //check if user exists
            if(usersDaoDb.get(user.getUid()).isEmpty()){
                return false;
            }

            String sql = "INSERT INTO stacks (uid, cid) VALUES (?, ?)";
            int rowsAffected = 0;
            try(PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)){
                for (Card card : cardPack.getCards()) {

                    preparedStatement.setInt(1, user.getUid());
                    preparedStatement.setString(2, card.getCid());
                    rowsAffected += preparedStatement.executeUpdate();
                }
                if(rowsAffected == cardPack.getCards().size()) {
                    return cardPackDaoDb.deleteById(cardPack.getPid());
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional get(int id) {
        return Optional.empty();
    }

    public Collection getAll() {
        return List.of();
    }

    public boolean save(Object o) {
        return false;
    }

    public boolean update(Object o) {
        return false;
    }

    public boolean delete(Object o) {
        return false;
    }
}
