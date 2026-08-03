package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "aGVsbG8td29ybGQtaGVsbG8td29ybGQtaGVsbG8td29ybGQtMTIzNDU2";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
    }

    @Test
    void testTokenRoundtrip() {
        String token = jwtService.generateToken("max@mail.de", "ADMIN");
        assertEquals("max@mail.de", jwtService.extractMail(token));
    }

    @Test
    void testTamperedTokenReturnsNull() {
        String token = jwtService.generateToken("max@mail.de", "ADMIN");
        String tampered = token.substring(0, token.length() - 4) + "XXXX";
        assertNull(jwtService.extractMail(tampered));
    }

    @Test
    void testExpiredTokenReturnsNull() {
        ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L);
        String token = jwtService.generateToken("max@mail.de", "ADMIN");
        assertNull(jwtService.extractMail(token));
    }
}
