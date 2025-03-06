package CrypticCreatures.api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import CrypticCreatures.persistence.dao.CardDaoDb;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import CrypticCreatures.core.models.cards.CardPack;
import CrypticCreatures.api.CardDTO;
import CrypticCreatures.core.models.cards.Card;
import CrypticCreatures.httpServer.http.HttpMethod;
import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.persistence.dao.CardPackDaoDb;
import CrypticCreatures.security.Authorizer;

public class CardPackController implements Controller {

    @Override
    public void handleRequest(HttpRequest request, BufferedWriter out) throws IOException {
        if(request.getMethod().equals(HttpMethod.GET)){
            sendMethodNotAllowed(out);
        }
        if(request.getMethod().equals(HttpMethod.POST)){
            if(request.getPath().startsWith("/packages")){
                createCardPack(request, out);
            }
            if(request.getPath().startsWith("/transactions/packages")){
                buyCardPack(request, out);
            }
        }
        if(request.getMethod().equals(HttpMethod.PUT)){
            sendMethodNotAllowed(out);
        }
        if(request.getMethod().equals(HttpMethod.DELETE)){
            sendMethodNotAllowed(out);
        }
    }

    private static void createCardPack(HttpRequest request, BufferedWriter out) throws IOException {
        if(Authorizer.isAdmin(request)){
            ObjectMapper mapper = new ObjectMapper();
            List<CardDTO> cardDTOs = mapper.readValue(request.getBody(), new TypeReference<List<CardDTO>>(){});
            CardPack cardPack = new CardPack();

            //check for duplicates in request
            Set<String> cardIds = new HashSet<>();
            for (CardDTO dto : cardDTOs) {
                if (!cardIds.add(dto.getId())) {
                    // Duplicate found in the incoming package
                    sendCardAlreadyExists(out);
                    return;
                }
            }

            //Check if ID already exists in database
            CardDaoDb cardDao = new CardDaoDb();
            for (CardDTO dto : cardDTOs) {
                Optional<Card> existingCard = cardDao.get(dto.getId());
                if(existingCard.isPresent()){
                    sendCardAlreadyExists(out);
                    return;
                }
            }

            for (CardDTO dto : cardDTOs) {
                Card card = Card.createRandomCard(dto.getId(), dto.getName(), dto.getDamage());
                cardPack.addCard(card);
            }

            CardPackDaoDb cardPackDaoDb = new CardPackDaoDb();

            if(cardPackDaoDb.save(cardPack)){
                //Success:
                sendCardPackCreated(out);
            }else{
                //Error:
                sendCardAlreadyExists(out);
            }
        }
        sendUserNotAdmin(out);
    }

    private static void buyCardPack(HttpRequest request, BufferedWriter out) throws IOException {

    }

    private static void sendCardPackCreated(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 201 Created\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("201 - Package and cards successfully created");
        out.flush();
    }

    private static void sendCardPackNotFound(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 404 Not Found\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("404 - Package Not Found");
        out.flush();
    }

    private static void sendUnauthorized(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 401 Unauthorized\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("401 - Access token is missing or invalid");
        out.flush();
    }

    private static void sendUserNotAdmin(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 403 Unauthorized\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("403 - Provided user is not \"admin\"");
        out.flush();
    }

    private static void sendMethodNotAllowed(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 405 Method Not Allowed\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("405 - Method Not Allowed");
        out.flush();
    }

    private static void sendCardAlreadyExists(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 409 Conflict\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("\r\n");
        out.write("409 - At least one card in the packages already exists");
        out.flush();
    }
}
