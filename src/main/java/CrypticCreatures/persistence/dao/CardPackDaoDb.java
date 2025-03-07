package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.cards.CardPack;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.persistence.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CardPackDaoDb {

    private final DbConnection dbConnection;

    public CardPackDaoDb() {
        this.dbConnection = DbConnection.getInstance();
    }

    public Optional<CardPack> get(int id) {
        try{
            int pid;
            int cost;
            String sql = "SELECT * FROM packages WHERE pid = ?";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)){
                preparedStatement.setInt(1, id);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        pid = resultSet.getInt("pid");
                        cost = resultSet.getInt("price");
                    }
                    else{
                        return Optional.empty();
                    }
                }
            }
            sql = "SELECT cid FROM package_cards WHERE pid = ?";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)){
                preparedStatement.setInt(1, pid);
                try (var resultSet = preparedStatement.executeQuery()) {
                    List<Card> cards = new ArrayList<>();
                    while (resultSet.next()) {
                        String cardId = resultSet.getString("cid");
                        CardDaoDb cardDaoDb = new CardDaoDb();
                        Optional<Card> card = cardDaoDb.get(cardId);
                        if (card.isPresent()) {
                            cards.add(card.get());
                        }
                    }
                    CardPack cardPack = new CardPack();
                    cardPack.setPid(pid);
                    cardPack.setCost(cost);
                    cardPack.setCards(cards);
                    return Optional.of(cardPack);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public Optional<CardPack> getOnePack() {
        try {
            String sql = "SELECT * FROM packages";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Return the first package found
                        return get(resultSet.getInt("pid"));
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean save(CardPack cardPack) {
        try {
            // Insert Package
            String sqlPackage = "INSERT INTO packages (price) VALUES (?)";
            try (PreparedStatement packageStmt = dbConnection.getConnection().prepareStatement(sqlPackage, Statement.RETURN_GENERATED_KEYS)) {
                packageStmt.setInt(1, cardPack.getCost());
                int rowsAffected = packageStmt.executeUpdate();
                if (rowsAffected == 0) {
                    return false;
                }
                try (var generatedKeys = packageStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        // set pid assigned by the database
                        int newPid = generatedKeys.getInt(1);
                        cardPack.setPid(newPid);
                    }
                }
            }

            //InsertCards into cards and package_cards tables
            CardDaoDb cardDaoDb = new CardDaoDb();
            String sqlPackageCards = "INSERT INTO package_cards (pid, cid) VALUES (?, ?)";
            try (PreparedStatement packageCardsStmt = dbConnection.getConnection().prepareStatement(sqlPackageCards)) {
                for (Card card : cardPack.getCards()) {
                    // Save the card if not already saved.
                    cardDaoDb.save(card);

                    packageCardsStmt.setInt(1, cardPack.getPid());
                    packageCardsStmt.setString(2, card.getCid());
                    packageCardsStmt.executeUpdate();
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean update(CardPack cardPack) {
        return false;
    }

    public boolean delete(CardPack cardPack) {
        return deleteById(cardPack.getPid());
    }

    public boolean deleteById(int id) {
        try{
            String sql = "DELETE FROM packages WHERE pid = ?";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)){
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
