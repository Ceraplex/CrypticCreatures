package CrypticCreatures.services;

import CrypticCreatures.core.models.User;

public class MatchMaker {
    private static final Object lock = new Object();
    private static User waitingUser = null;
    private static User matchedOpponent = null;

    public static User waitForOpponent(User user) {
        synchronized(lock) {
            if (waitingUser == null) {
                waitingUser = user;
                try {
                    //Wait max 30s
                    lock.wait(30000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    waitingUser = null;
                    return null;
                }
                User opponent = matchedOpponent;

                matchedOpponent = null;

                return opponent;
            } else {

                User opponent = waitingUser;
                waitingUser = null;

                matchedOpponent = user;

                lock.notify();
                return opponent;
            }
        }
    }

}
