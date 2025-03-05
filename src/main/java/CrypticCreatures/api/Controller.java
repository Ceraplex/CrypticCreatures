package CrypticCreatures.api;

import CrypticCreatures.httpServer.http.HttpRequest;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Controller {
    void handleRequest(HttpRequest request, BufferedWriter out) throws IOException;
}
