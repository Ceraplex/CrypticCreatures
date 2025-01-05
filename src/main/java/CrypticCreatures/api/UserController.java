package CrypticCreatures.api;

import java.io.BufferedWriter;
import java.io.IOException;

import CrypticCreatures.core.models.User;
import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.persistence.Database;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController {

    // METHOD: POST
    public static void registerUser(HttpRequest request, BufferedWriter out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(request.getBody(), User.class);

        //TODO: add user to DB and reply
        /*if(database.addUser(user)){
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
        }*/
    }

    //METHOD: GET
    public static void getUserData(HttpRequest request, BufferedWriter out) throws IOException{
        String userId = request.getPath().split("/")[2];
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("{\"id\": " + userId + ", \"name\": \"John Doe\"}");
        out.flush();
    }

    //METHOD: PUT
    private static void updateUser(HttpRequest request, BufferedWriter out) throws IOException {
        //TODO: implement update user
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
}
