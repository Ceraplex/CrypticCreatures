package CrypticCreatures.security;

import CrypticCreatures.httpServer.http.HttpRequest;

public class Authorizer {

    public static boolean authorizeHttpRequest(HttpRequest request) {
        String token = getToken(request);
        if (token == null){
            return false;
        }
        return isTokenValid(token);
    }

    public static String getToken(HttpRequest request) {
        String rawToken = request.getHeaders().get("authorization");
        if (rawToken == null){
            return null;
        }
        if(!request.getHeaders().get("authorization").contains("Bearer ")){
            return null;
        }
        if(request.getHeaders().get("authorization").split("Bearer ")[1] == null){
            return null;
        }
        return request.getHeaders().get("authorization").split("Bearer ")[1];
    }

    public static String getUsernameFromRequest(HttpRequest request) {
        String token = getToken(request);
        if (token == null) {
            return null;
        }
        return getTokenUsername(token);

    }

    public static boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }
        return token.endsWith("-mtcgToken");
    }

    public static String getTokenUsername(String token) {
        if (token == null) {
            return null;
        }
        return token.split("-mtcgToken")[0];
    }

    public static boolean isAdmin(HttpRequest request) {
        String token = getToken(request);
        if (token == null) {
            return false;
        }
        if(isTokenValid(token)){
            return token.equals("admin-mtcgToken");
        }
        return false;
    }
}
