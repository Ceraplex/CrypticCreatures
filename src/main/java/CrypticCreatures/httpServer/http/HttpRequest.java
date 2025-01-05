package CrypticCreatures.httpServer.http;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class HttpRequest {
    private HttpMethod method;
    private String path;
    private Map<String, String> headers;
    private String body;

    public HttpRequest() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String name, String value) {
        this.headers.put(name.toLowerCase(), value);
    }

    public void setHttpMethod(String method) {
        //TODO: verify case sensitivity of method
        this.method = HttpMethod.valueOf(method);
    }
}
