package CrypticCreatures.api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;

import CrypticCreatures.core.models.User;
import CrypticCreatures.httpServer.http.HttpMethod;
import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.persistence.dao.UsersDaoDb;
import CrypticCreatures.security.Authorizer;

public class UserController implements Controller {

    @Override
    public void handleRequest(HttpRequest request, BufferedWriter out) throws IOException {
        if (request.getMethod().equals(HttpMethod.GET)) {
            getUserData(request, out);
        }
        if (request.getMethod().equals(HttpMethod.POST)) {
            registerUser(request, out);
        }
        if (request.getMethod().equals(HttpMethod.PUT)) {
            updateUser(request, out);
        }
        if (request.getMethod().equals(HttpMethod.DELETE)) {
            //TODO: optional implement
            sendMethodNotAllowed(out);
        }
    }
    // METHOD: POST
    public static void registerUser(HttpRequest request, BufferedWriter out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(request.getBody(), User.class);

        UsersDaoDb usersDao = new UsersDaoDb();

        if(usersDao.save(user)){
            //Success:
            out.write("HTTP/1.1 201 Created\r\n");
            out.write("\r\n");
            out.flush();

            //TODO: remove debug info
            System.out.println("user registered");
            System.out.println(user.toString());

        }else{
            //Error:
            out.write("HTTP/1.1 409 Conflict\r\n");
            out.write("Content-Type: text/plain\r\n");
            out.write("\r\n");
            out.write("409 - Conflict: Username already exists\r\n");
            out.flush();

            //TODO: remove debug info
            System.out.println("user already exists");
            System.out.println(user.toString());
        }
    }

    //TODO: test
    //METHOD: GET
    public static void getUserData(HttpRequest request, BufferedWriter out) throws IOException{
        if(Authorizer.authorizeHttpRequest(request)){
            String username = Authorizer.getUsernameFromRequest(request);
            UsersDaoDb usersDao = new UsersDaoDb();
            try{
                User user = usersDao.getUserByUsername(username);
                if (user != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(user);
                    out.write("HTTP/1.1 200 OK\r\n\r\n" + json);
                } else {
                    sendNotFound(out);
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        else {
            sendUnauthorized(out);
        }
    }

    //METHOD: PUT
    private static void updateUser(HttpRequest request, BufferedWriter out) throws IOException {
        if(Authorizer.authorizeHttpRequest(request)){
            //TODO: update user data
        }
        else {
            sendUnauthorized(out);
        }
    }


    //Example request processing #######################################################################################
    //TODO: remove before hand-in
    private static void sendUserList(BufferedWriter out) throws IOException {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("[{\"id\": 1, \"name\": \"John\"}, {\"id\": 2, \"name\": \"Jane\"}]");
        out.flush();
    }

    private static void sendNotFound(BufferedWriter out) throws IOException {
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
        out.write("401 - Unauthorized");
        out.flush();
    }
}
