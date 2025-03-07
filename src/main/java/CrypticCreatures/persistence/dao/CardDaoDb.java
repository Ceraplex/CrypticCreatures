package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.User;
import CrypticCreatures.core.models.cards.*;
import CrypticCreatures.core.models.cards.monster.MonsterCard;
import CrypticCreatures.core.models.cards.spell.SpellCard;
import CrypticCreatures.persistence.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDaoDb {

    private final DbConnection dbConnection;
    public CardDaoDb() {
        this.dbConnection = DbConnection.getInstance();
    }

    public Optional<Card> get(String id) {
        String sql = "SELECT * FROM cards WHERE cid = ?";
        try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String cid = resultSet.getString("cid");
                    String name = resultSet.getString("name");
                    double damage = resultSet.getDouble("dmg");
                    String element_type = resultSet.getString("element_type");
                    String monster_type = resultSet.getString("monster_type");

                    ElementType elementType = ElementType.valueOf(element_type);

                    if(monster_type.isEmpty()){
                        return Optional.of(new SpellCard(cid, name, damage, elementType));
                    } else {
                        CreatureType creatureType = CreatureType.valueOf(monster_type);
                        return Optional.of(new MonsterCard(cid, name, damage, elementType, creatureType));
                    }
                } else {
                    return Optional.empty();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean save(Card card) {
        String sql = "INSERT INTO cards (cid, name, dmg, element_type, monster_type) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, card.getCid());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setDouble(3, card.getDmg());
            preparedStatement.setString(4, card.getElementType().toString());

            // For SpellCard, leave monster_type empty; for MonsterCard, set the creature type.
            if (card instanceof MonsterCard) {
                MonsterCard monsterCard = (MonsterCard) card;
                preparedStatement.setString(5, monsterCard.getCreatureType().toString());
            } else {
                preparedStatement.setString(5, "");
            }

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean update(Card card) {
        try{
            String sql = "UPDATE cards SET name = ?, dmg = ?, element_type = ?, monster_type = ? WHERE cid = ?";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, card.getName());
                preparedStatement.setDouble(2, card.getDmg());
                preparedStatement.setString(3, card.getElementType().toString());

                // For SpellCard, leave monster_type empty; for MonsterCard, set the creature type.
                if (card instanceof MonsterCard) {
                    MonsterCard monsterCard = (MonsterCard) card;
                    preparedStatement.setString(4, monsterCard.getCreatureType().toString());
                } else {
                    preparedStatement.setString(4, "");
                }

                preparedStatement.setString(5, card.getCid());
                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Card card) {
        String sql = "DELETE FROM cards WHERE cid = ?";
        try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, card.getCid());
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Card> getAllByUser(User user) {
        try{
            String sql = "SELECT * FROM stacks WHERE uid = ?";
            List<Card> cards = new ArrayList<>();
            try(PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, user.getUid());
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String cid = resultSet.getString("cid");
                        Optional<Card> card = get(cid);
                        if(card.isPresent()){
                            cards.add(card.get());
                        }
                    }
                    return cards;
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean userHasCard(Integer uid, String cid) {
        try{
            String sql = "SELECT * FROM stacks WHERE uid = ? AND cid = ?";
            try(PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, uid);
                preparedStatement.setString(2, cid);
                try(ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCardOwner(Integer uid, String cid) {
        try{
            String sql = "UPDATE stacks SET uid = ? WHERE cid = ?";
            try (PreparedStatement preparedStatement = dbConnection.getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, uid);
                preparedStatement.setString(2, cid);
                int rowsUpdated = preparedStatement.executeUpdate();
                return rowsUpdated > 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
