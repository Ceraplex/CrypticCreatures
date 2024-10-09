package CrypticCreatures.api;

import CrypticCreatures.persistence.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Dispatcher implements Runnable {

    private Database database;
    private final Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    public Dispatcher(Socket clientSocket, Database database) {
        this.clientSocket = clientSocket;
        this.database = database;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            StringBuilder headerBuilder = new StringBuilder();
            String inputLine;
            int contentLength = 0;

            // Read the request line
            inputLine = in.readLine();
            while (inputLine != null && inputLine.isEmpty()) {
                inputLine = in.readLine();
            }


            String requestLine = inputLine;

            // Read HTTP request headers
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                headerBuilder.append(inputLine).append("\r\n");
                // Get Content-Length to read the body later
                if (inputLine.toLowerCase().startsWith("content-length:")) {
                    String[] parts = inputLine.split(":", 2);
                    if (parts.length == 2) {
                        contentLength = Integer.parseInt(parts[1].trim());
                    }
                }
            }

            String body = "";
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                int charsRead = in.read(bodyChars, 0, contentLength);
                body = new String(bodyChars, 0, charsRead);
            }


            if(requestLine != null && !requestLine.isEmpty()){

                //Parse method and path
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                if(path.startsWith("/users") || path.startsWith("/sessions")){
                    UserController.handleRequest(method, path, body, out, database);
                }
                if(path.startsWith("/packages")){
                    PackageController.handleRequest(method, path, body, out,  database);
                }
                if(path.startsWith("/cards")){
                    CardController.handleRequest(method, path, body, out, database);
                }
                if(path.startsWith("/deck")){
                    DeckController.handleRequest(method, path, body, out, database);
                }
                if(path.startsWith("/tradings")){
                    TradingController.handleRequest(method, path, body, out, database);
                }
                if(path.startsWith("/scoreboard") || path.startsWith("/stats")){
                    StatsController.handleRequest(method, path, body, out, database);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

