package CrypticCreatures.api;

import java.io.BufferedWriter;
import java.io.IOException;

import CrypticCreatures.core.models.User;
import CrypticCreatures.persistence.Database;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController {

    public static void handleRequest(String method, String path, String body, BufferedWriter out, Database database) throws IOException {
        if (method.equals("GET")) {
            //TODO: replace GET section with correct routes
            if (path.equals("/users")) {
                sendUserList(out);
            } else if (path.matches("/users/\\d+")) {
                sendUserDetails(out, path);
            } else {
                sendNotFound(out);
            }

        } else if (method.equals("POST")) {
            if(path.equals("/users")) {
                registerUser(body, out, database);
            } else if (path.equals("/sessions")) {
                loginUser(body, out, database);
            }
        } else {
            sendMethodNotAllowed(out);
        }
    }


    private static void registerUser(String body, BufferedWriter out, Database database) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(body, User.class);
        if(database.addUser(user)){
            out.write("HTTP/1.1 201 Created\r\n");
            out.write("\r\n");
            out.flush();

            //TODO: remove debug info
            System.out.println("user registered");
            System.out.println(user.toString());

        }else{
            out.write("HTTP/1.1 409 Conflict\r\n");
            out.write("Content-Type: text/plain\r\n");
            out.write("\r\n");
            out.write("409 - Conflict: Username already exists\r\n");

            System.out.println("user already exists");
            System.out.println(user.toString());
            out.flush();
        }
    }

    private static void loginUser(String body, BufferedWriter out, Database database) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(body, User.class);
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

    private static void sendUserDetails(BufferedWriter out, String path) throws IOException {
        String userId = path.split("/")[2];
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("\r\n");
        out.write("{\"id\": " + userId + ", \"name\": \"John Doe\"}");
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
