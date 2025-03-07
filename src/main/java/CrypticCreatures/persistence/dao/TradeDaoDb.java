package CrypticCreatures.persistence.dao;

import CrypticCreatures.core.models.Trade;
import CrypticCreatures.persistence.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TradeDaoDb {
    private final DbConnection dbConnection;

    public TradeDaoDb() {
        this.dbConnection = DbConnection.getInstance();
    }

    public boolean createTrade(Trade trade) {
        String sql = "INSERT INTO trades (trade_id, owner, offered_card, required_type, min_damage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, trade.getTid());
            ps.setInt(2, trade.getUid());
            ps.setString(3, trade.getOfferedCardId());
            ps.setString(4, trade.getRequiredType());
            ps.setInt(5, trade.getMinDamage());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Trade> getTradeById(String tid) {
        String sql = "SELECT * FROM trades WHERE tid = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, tid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Trade(
                            rs.getString("tid"),
                            rs.getInt("owner"),
                            rs.getString("cid"),
                            rs.getString("required_type"),
                            rs.getInt("min_damage")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

    public List<Trade> getAllTrades() {
        List<Trade> trades = new ArrayList<>();
        String sql = "SELECT * FROM trades";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                trades.add(new Trade(
                        rs.getString("tid"),
                        rs.getInt("uid"),
                        rs.getString("cid"),
                        rs.getString("required_type"),
                        rs.getInt("min_damage")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trades;
    }

    public boolean deleteTrade(String tradeId, int uid) {
        String sql = "DELETE FROM trades WHERE tid = ? AND uid = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.setString(1, tradeId);
            ps.setInt(2, uid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
