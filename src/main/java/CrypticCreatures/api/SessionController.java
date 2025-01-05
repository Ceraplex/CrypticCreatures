package CrypticCreatures.api;

import CrypticCreatures.core.models.User;
import CrypticCreatures.httpServer.http.HttpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.IOException;

public class SessionController {

    private static void loginUser(HttpRequest request, BufferedWriter out) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(request.getBody(), User.class);

        //TODO verify user in DB for if statement below
        if(){
            out.write("HTTP/1.1 200 Success\r\n");
            out.write("Content-Type: text/plain\r\n");
            out.write("\r\n");
            out.write(user.getToken());
            out.flush();
        }else{
            out.write("HTTP/1.1 401 Unauthorized\r\n");
            out.write("Content-Type: text/plain\r\n");
            out.write("\r\n");
            out.write("Login failed\r\n");
        }
    }

}
