package CrypticCreatures.api;


import CrypticCreatures.httpServer.http.HttpRequestParser;
import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.httpServer.http.HttpMethod;

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

            HttpRequest request = HttpRequestParser.buildHttpRequest(in, out);


            if(request.getPath().startsWith("/users")){
                //POST, GET, PUT
                handleUsers(request);
            }
            if(request.getPath().startsWith("/sessions")){
                //POST
                handleSessions(request);
            }
            if(request.getPath().startsWith("/packages")){
                //POST
                handlePackages(request);
            }
            if(request.getPath().startsWith("/transactions/packages")){
                //POST
                handleTransaction(request);
            }
            if(request.getPath().startsWith("/cards")){
                //GET
                handleCards(request);
            }
            if(request.getPath().startsWith("/deck")){
                //GET, GET format plain, PUT
                handleDeck(request);
            }
            if(request.getPath().startsWith("/stats")){
                //GET
                handleStats(request);
            }
            if(request.getPath().startsWith("/tradings")){
                //GET, POST, PUT, DELETE
                handleTradings(request);
            }
            if(request.getPath().startsWith("/scoreboard") || request.getPath().startsWith("/stats")){
                //GET
                handleScoreboard(request);
            }
            if(request.getPath().startsWith("/battles")){
                //POST
                handleBattle(request);
            }


        } catch (IOException e) {
            //TODO: check throws further down
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void handleUsers(HttpRequest request) throws IOException {
        if(request.getMethod().equals(HttpMethod.POST)){
            UserController.registerUser(request, out);
        } else if(request.getMethod().equals(HttpMethod.GET)){
            UserController.getUserData(request, out);
        } else if(request.getMethod().equals(HttpMethod.PUT)){
            UserController.getUserData(request, out);
        }
    }

    private void handleSessions(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.POST)){
            //Create session
        }
    }

    private void handlePackages(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.POST)){
            //Create package
        }
    }

    private void handleTransaction(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.POST)){
            //Create transaction
        }
    }

    private void handleCards(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.GET)){
            //Get card
        }
    }

    private void handleDeck(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.GET)){
            //Get deck
        } else if(request.getMethod().equals(HttpMethod.PUT)){
            //Update deck
        }
    }

    private void handleTradings(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.POST)){
            //Create trading
        } else if(request.getMethod().equals(HttpMethod.GET)){
            //Get trading
        } else if(request.getMethod().equals(HttpMethod.PUT)){
            //Update trading
        } else if(request.getMethod().equals(HttpMethod.DELETE)){
            //Delete trading
        }
    }

    private void handleScoreboard(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.GET)){
            //Get scoreboard
        }
    }

    private void handleStats(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.GET)){
            //Get stats
        }
    }

    private void handleBattle(HttpRequest request){
        if(request.getMethod().equals(HttpMethod.POST)){
            //Create battle
        }
    }

}

