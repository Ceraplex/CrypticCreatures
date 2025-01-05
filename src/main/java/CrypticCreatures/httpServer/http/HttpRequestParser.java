package CrypticCreatures.httpServer.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class HttpRequestParser {

    public static HttpRequest buildHttpRequest(BufferedReader in) throws IOException {

        // ### Read Path and Method ###
        String requestLine = in.readLine();
        while (requestLine != null && requestLine.isEmpty()) {
            requestLine = in.readLine();
        }
        // If no request line, return null or send an error response
        if (requestLine == null || requestLine.isEmpty()) {
            return null;
        }

        // Split the request line into: Method and Path
        String[] requestParts = requestLine.split(" ");
        String method = requestParts[0];
        String path = requestParts[1];



        // Create our HttpRequest object
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHttpMethod(method);
        httpRequest.setPath(path);


        // ### Read headers ###
        int contentLength = 0;
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            int idx = headerLine.indexOf(":");
            if (idx != -1) {
                String headerName = headerLine.substring(0, idx).trim();
                String headerValue = headerLine.substring(idx + 1).trim();

                httpRequest.addHeader(headerName, headerValue);

                // If it's the Content-Length header, capture the value as an int
                if ("content-length".equalsIgnoreCase(headerName)) {
                    try {
                        contentLength = Integer.parseInt(headerValue);
                    } catch (NumberFormatException e) {
                        contentLength = 0;
                    }
                }
            }
        }

        // ### Read body ###
        if (contentLength > 0) {
            char[] bodyChars = new char[contentLength];
            int charsRead = in.read(bodyChars, 0, contentLength);
            if (charsRead > 0) {
                String body = new String(bodyChars, 0, charsRead);
                httpRequest.setBody(body);
            }
        }

        // 4) Return the fully parsed request
        return httpRequest;
    }
}