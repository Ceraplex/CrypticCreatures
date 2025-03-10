package CrypticCreatures.httpServer;

import CrypticCreatures.api.Dispatcher;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(new Dispatcher(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}