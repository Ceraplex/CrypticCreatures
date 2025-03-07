package CrypticCreatures.api;


import CrypticCreatures.httpServer.http.HttpRequestParser;
import CrypticCreatures.httpServer.http.HttpRequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Dispatcher implements Runnable {


    private final Socket clientSocket;

    public Dispatcher(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            HttpRequest request = HttpRequestParser.buildHttpRequest(in);
            //TODO: remove debug info
            System.out.println("Request received: " + request.getMethod() + " " + request.getPath());
            System.out.println("Headers: " + request.getHeaders());
            System.out.println("Body: " + request.getBody());

            Controller controller = getControllerForPath(request.getPath());
            controller.handleRequest(request, out);

        } catch (IOException e) {
            //TODO: check throws further down
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Controller getControllerForPath(String path) {
        if(path.startsWith("/users")||path.startsWith("/sessions")||path.startsWith("/deck")){
            return new UserController();
        }
        if(path.startsWith("/packages") || path.startsWith("/transactions/packages")){
            return new CardPackController();
        }
        if(path.startsWith("/tradings")){
            return new TradingController();
        }
        if(path.startsWith("/battles")){
            return new BattleController();
        }
        if(path.startsWith("/reset")){
            return new ResetController();
        }
        return null;
    }
}

