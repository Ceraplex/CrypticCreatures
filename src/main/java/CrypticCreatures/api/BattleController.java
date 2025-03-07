package CrypticCreatures.api;

import CrypticCreatures.services.Battle;
import CrypticCreatures.core.models.User;
import CrypticCreatures.httpServer.http.HttpMethod;
import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.persistence.dao.BattleDaoDb;
import CrypticCreatures.persistence.dao.UsersDaoDb;
import CrypticCreatures.security.Authorizer;
import CrypticCreatures.services.MatchMaker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BattleController implements Controller {
    @Override
    public void handleRequest(HttpRequest request, BufferedWriter out) throws IOException {
        if (request.getMethod().equals(HttpMethod.GET)) {
            sendMethodNotAllowed(out);
        }
        if (request.getMethod().equals(HttpMethod.POST)) {
            conductBattle(request, out);
        }
        if (request.getMethod().equals(HttpMethod.PUT)) {
            sendMethodNotAllowed(out);
        }
        if (request.getMethod().equals(HttpMethod.DELETE)) {
            sendMethodNotAllowed(out);
        }
    }

    private static void conductBattle(HttpRequest request, BufferedWriter out) throws IOException {
        if(!Authorizer.authorizeHttpRequest(request)) {
            sendUnauthorized(out);
            return;
        }

        String username = Authorizer.getUsernameFromRequest(request);
        BattleDaoDb battleDaoDb = new BattleDaoDb();
        UsersDaoDb usersDaoDb = new UsersDaoDb();

        try{
            Optional<User> userOpt = usersDaoDb.getUserByUsername(username);
            if(userOpt.isEmpty()){
                sendUserNotFound(out);
                return;
            }

            User user1 = userOpt.get();
            //using lock to wait for opponent
            User  user2 = MatchMaker.waitForOpponent(user1);
            if(user2 == null){
                sendUserNotFound(out);
                return;
            }
            System.out.println("BATTLE+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

            Battle battle = new Battle();
            battle.fight(user1, user2);
            sendBattleCarriedOut(out);
        } catch (SQLException e) {
            e.printStackTrace();
            out.write("HTTP/1.1 500 Internal Server Error\r\n");
            out.flush();
        }
    }

    public static Optional<User> getUserByElo(int elo) {
        BattleDaoDb battleDaoDb = new BattleDaoDb();
        List<User> users = battleDaoDb.getAll();
        for (User user : users) {
            //looks for Users within 100 elo difference
            if (Math.abs(user.getElo() - elo) < 100) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private static void sendBattleCarriedOut(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("200 - The battle has been carried out successfully.");
    }

    private static void userAlreadyExists(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 409 Conflict\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("409 - User with same username already registered");
        out.flush();
    }

    private static void sendUserNotFound(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 404 Not Found\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("404 - User Not Found");
        out.flush();
    }

    private static void sendMethodNotAllowed(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 405 Method Not Allowed\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("405 - Method Not Allowed");
        out.flush();
    }

    private static void sendUnauthorized(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 401 Unauthorized\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("401 - Access token is missing or invalid");
        out.flush();
    }
}
