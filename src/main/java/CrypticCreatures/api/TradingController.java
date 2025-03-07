package CrypticCreatures.api;

import CrypticCreatures.core.models.User;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.httpServer.http.HttpMethod;
import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.persistence.dao.CardDaoDb;
import CrypticCreatures.persistence.dao.TradeDaoDb;
import CrypticCreatures.core.models.Trade;
import CrypticCreatures.persistence.dao.UsersDaoDb;
import CrypticCreatures.security.Authorizer;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.text.html.Option;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TradingController implements Controller {

    @Override
    public void handleRequest(HttpRequest request, BufferedWriter out) throws IOException {

        if (request.getMethod().equals(HttpMethod.GET)) {
            listTrades(request, out);
        }
        if (request.getMethod().equals(HttpMethod.POST)) {
            if (request.getPath().startsWith("/tradings/")) {
                acceptTrade(request, out);
            } else {
                createTrade(request, out);
            }
        }
        if (request.getMethod().equals(HttpMethod.PUT)) {
            sendMethodNotAllowed(out);
        }
        if (request.getMethod().equals(HttpMethod.DELETE)) {
            deleteTrade(request, out);
        }
    }

    private void createTrade(HttpRequest request, BufferedWriter out) throws IOException {
        if(!Authorizer.authorizeHttpRequest(request)){
            sendUnauthorized(out);
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        Trade trade = mapper.readValue(request.getBody(), Trade.class);
        UsersDaoDb usersDao = new UsersDaoDb();
        TradeDaoDb tradeDao = new TradeDaoDb();

        try{
            Optional<User> user = usersDao.getUserByUsername(Authorizer.getUsernameFromRequest(request));
            if(user.isPresent()){
                trade.setUid(user.get().getUid());
            } else {
                sendUserNotFound(out);
                return;
            }
        }catch (SQLException e){
            e.printStackTrace();
            out.write("HTTP/1.1 500 Internal Server Error\r\n");
            out.flush();
        }

        if (tradeDao.createTrade(trade)) {
            sendTradeCreated(out);
        } else {
            out.write("HTTP/1.1 500 Internal Server Error\r\n\r\nFailed to create trade.");
        }
    }



    private void listTrades(HttpRequest request, BufferedWriter out) throws IOException {
        if(!Authorizer.authorizeHttpRequest(request)){
            sendUnauthorized(out);
            return;
        }
        TradeDaoDb tradeDao = new TradeDaoDb();
        List<Trade> trades = tradeDao.getAllTrades();

        if (trades.isEmpty()) {
            out.write("HTTP/1.1 204 - The request was fine, but there are no trading deals available");
        } else {
            StringBuilder response = new StringBuilder("HTTP/1.1 200 OK\r\n\r\n");
            for (Trade trade : trades) {
                response.append("Id: ").append(trade.getTid())
                        .append(", CardToTrade: ").append(trade.getOfferedCardId())
                        .append(", Type: ").append(trade.getRequiredType())
                        .append(", MinimumDamage: ").append(trade.getMinDamage())
                        .append("\n");
            }
            out.write(response.toString());
            out.flush();
        }
    }

    private void deleteTrade(HttpRequest request, BufferedWriter out) throws IOException {
        if(!Authorizer.authorizeHttpRequest(request)){
            sendUnauthorized(out);
            return;
        }
        UsersDaoDb usersDao = new UsersDaoDb();
        TradeDaoDb tradeDao = new TradeDaoDb();
        String username = Authorizer.getUsernameFromRequest(request);

        try{
            Optional<User> user  = usersDao.getUserByUsername(username);
            if(!user.isPresent()){
                sendUserNotFound(out);
                return;
            }

            String tradeId = request.getPath().split("/")[2];

            if (tradeDao.deleteTrade(tradeId, user.get().getUid())) {
                out.write("HTTP/1.1 200 OK\r\n\r\nTrade deleted successfully.");
            } else {
                out.write("HTTP/1.1 404 Not Found\r\n\r\nTrade not found or not authorized.");
            }
        } catch (SQLException e){
            e.printStackTrace();
            out.write("HTTP/1.1 500 Internal Server Error\r\n");
            out.flush();
        }
    }

    private void acceptTrade(HttpRequest request, BufferedWriter out) throws IOException {
        if(!Authorizer.authorizeHttpRequest(request)){
            sendUnauthorized(out);
            return;
        }

        String path = request.getPath();
        String[] pathParts = path.split("/");
        if (pathParts.length < 3) {
            out.write("HTTP/1.1 400 Bad Request\r\n\r\nInvalid path format.");
            return;
        }
        String tid = pathParts[2];

        String buyerCardId = request.getBody().replace("\"", ""); // Entferne Anführungszeichen

        TradeDaoDb tradeDao = new TradeDaoDb();
        CardDaoDb cardDao = new CardDaoDb();
        UsersDaoDb usersDao = new UsersDaoDb();

        try{
            Optional<User> user = usersDao.getUserByUsername(Authorizer.getUsernameFromRequest(request));
            Optional<Trade> optTrade = tradeDao.getTradeById(tid);
            if(optTrade.isPresent()){
                User buyer = user.get();
                Trade trade = optTrade.get();
                Optional<User> seller = usersDao.get(trade.getUid());

                if(!cardDao.userHasCard(buyer.getUid(), buyerCardId) || !cardDao.userHasCard(seller.get().getUid(), trade.getOfferedCardId())){
                    sendCardNotPossible(out);
                    return;
                }
                Optional<Card> buyerCard = cardDao.get(buyerCardId);
                Optional<Card> sellerCard = cardDao.get(trade.getOfferedCardId());
                if(buyerCard.isPresent() && sellerCard.isPresent()){
                    if(buyerCard.get().getDmg() >= trade.getMinDamage()){
                        cardDao.updateCardOwner(seller.get().getUid(), buyerCardId);
                        cardDao.updateCardOwner(buyer.getUid(), trade.getOfferedCardId());
                        out.write("HTTP/1.1 201 Trade finished\r\n\r\n");
                        out.write("Content-Type: text/plain\r\n");
                        out.write("\r\n");
                        out.write("201 - Trade finished successfully.");
                        out.flush();
                    }
                } else {
                    sendCardNotPossible(out);
                    return;
                }


            } else {
                sendTradeNotFound(out);
            }

        } catch (SQLException e){
            e.printStackTrace();
            out.write("HTTP/1.1 500 Internal Server Error\r\n");
            out.flush();
        }


    }

    private void sendTradeNotFound(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 410 Trade Not Found\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("410 - Trade Not Found");
        out.flush();
    }

    private void sendTradeCreated(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 201 OK\r\n\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("201 - Trading deal successfully created");
        out.flush();
    }

    private static void sendUserCreated(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 201 Created\r\n");

        out.write("201 - Created: User created");
        out.flush();
    }

    private static void sendCardNotPossible(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 403 Conflict\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("403 - The deal contains a card that is not owned by the user or locked in the deck.");
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

    private static void sendTradeAlreadyExists(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 409 Unauthorized\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("409 - A deal with this deal ID already exists.");
        out.flush();
    }
}
