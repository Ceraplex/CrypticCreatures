package CrypticCreatures.api;

import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.persistence.DbConnection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetController implements Controller {

    private final DbConnection dbConnection;

    public ResetController() {
        // Get the singleton instance of your custom DbConnection wrapper
        this.dbConnection = DbConnection.getInstance();
    }


    public void reset(){
        String sql = "TRUNCATE TABLE users, cards, profile_pages, decks, stacks, packages, package_cards RESTART IDENTITY CASCADE;";
        try (PreparedStatement ps = dbConnection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRequest(HttpRequest request, BufferedWriter out) throws IOException {
        reset();
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("Database reset");
        out.flush();
    }
}
