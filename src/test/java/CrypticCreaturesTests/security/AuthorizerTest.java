package CrypticCreaturesTests.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import java.util.Map;

import CrypticCreatures.httpServer.http.HttpRequest;
import CrypticCreatures.security.Authorizer;

public class AuthorizerTest {

    @Test
    public void testIsTokenValid_withValidToken() {
        String validToken = "kienboec-mtcgToken";
        assertTrue(Authorizer.isTokenValid(validToken));
    }

    @Test
    public void testIsTokenValid_withInvalidToken() {
        String invalidToken = "kienboec-token";
        assertFalse(Authorizer.isTokenValid(invalidToken));
    }

    @Test
    public void testGetTokenUsername() {
        String token = "kienboec-mtcgToken";
        String username = Authorizer.getTokenUsername(token);
        assertEquals("kienboec", username);
    }

    @Test
    public void testGetUsernameFromRequest() {
        HttpRequest request = mock(HttpRequest.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer kienboec-mtcgToken");
        when(request.getHeaders()).thenReturn(headers);

        assertEquals("kienboec", Authorizer.getUsernameFromRequest(request));
    }

    @Test
    public void testGetToken_withBearerPrefix() {
        // Create a mock HttpRequest
        HttpRequest request = mock(HttpRequest.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer kienboec-mtcgToken");
        when(request.getHeaders()).thenReturn(headers);

        String token = Authorizer.getToken(request);
        assertEquals("kienboec-mtcgToken", token);
    }

    @Test
    public void testGetToken_whenHeaderMissing() {
        HttpRequest request = mock(HttpRequest.class);
        Map<String, String> headers = new HashMap<>();
        // No Authorization header added
        when(request.getHeaders()).thenReturn(headers);

        String token = Authorizer.getToken(request);
        assertNull(token);
    }

    @Test
    public void testAuthorizeHttpRequest_valid() {
        HttpRequest request = mock(HttpRequest.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer kienboec-mtcgToken");
        when(request.getHeaders()).thenReturn(headers);

        assertTrue(Authorizer.authorizeHttpRequest(request));
    }

    @Test
    public void testAuthorizeHttpRequest_invalid() {
        HttpRequest request = mock(HttpRequest.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "invalid-token");
        when(request.getHeaders()).thenReturn(headers);

        assertFalse(Authorizer.authorizeHttpRequest(request));
    }
}