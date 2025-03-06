package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.cards.CardPack;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.persistence.DbConnection;

import java.sql.PreparedStatement;
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
                        cost = resultSet.getInt("cost");
                    }
                    else{
                        return Optional.empty();
                    }
                }
            }
            sql = "SELECT card_id FROM package_cards WHERE package_id = ?";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)){
                preparedStatement.setInt(1, pid);
                try (var resultSet = preparedStatement.executeQuery()) {
                    List<Card> cards = new ArrayList<>();
                    while (resultSet.next()) {
                        String cardId = resultSet.getString("card_id");
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

    public Collection<CardPack> getAll() {
        return List.of();
    }

    public boolean save(CardPack cardPack) {
        return false;
    }

    public boolean update(CardPack cardPack, String[] params) {
        return false;
    }

    public boolean delete(CardPack cardPack) {
        return false;
    }
}
