package pe.entelgy.backend.evfinal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private UserDetails userDetails;
    private String username = "testUser";
    private String token;

    @BeforeEach
    void setUp() {
        userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("password")
                .authorities(Collections.singletonList(() -> "ROLE_USER"))
                .build();
        
        token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testExtractUsername() {
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testIsTokenValid() {
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenInvalid() {
        String validToken = jwtService.generateToken(userDetails);

        String invalidToken = validToken.substring(0, validToken.lastIndexOf('.') + 1) + "invalidSignature";

        assertThrows(SignatureException.class, () -> jwtService.isTokenValid(invalidToken, userDetails));
    }

    @Test
    public void testExtractAuthorities() {
        Collection<GrantedAuthority> authorities = jwtService.extractAuthorities(token);
        assertEquals(1, authorities.size());
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        assertEquals("ROLE_USER", role);
    }

    @Test
    public void testExtractExpiration() {
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertTrue(expiration.after(new Date()));
    }
}