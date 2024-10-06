package CrypticCreatures.api;

import CrypticCreatures.api.UserController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Dispatcher implements Runnable {

    private final Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    public Dispatcher(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            StringBuilder headerBuilder = new StringBuilder();
            StringBuilder bodyBuilder = new StringBuilder();

            //Read HTTP request header
            String inputLine;
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()){
                headerBuilder.append(inputLine).append(System.lineSeparator());
            }

            //Read HTTP request body
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()){
                bodyBuilder.append(inputLine).append(System.lineSeparator());
            }
            String body = bodyBuilder.toString();


            String requestLine = headerBuilder.toString().split(System.lineSeparator())[0];
            if(requestLine != null && !requestLine.isEmpty()){

                //Parse method and path
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                if(path.equals("/users")){
                    UserController.handleRequest(method, path, body, out);
                }
                //TODO: Route to other controllers
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

