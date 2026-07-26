package com.example.Invoice_Management_SPRING_REACT_PSQL.Security; 
import io.jsonwebtoken.Claims;										
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;




@Service 
public class JwtService { 

    @Value("${app.jwt.secret}") 
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs; 
    private SecretKey getSigningKey() {                                                // Base64-Secret in HMAC-Key umwandeln
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));                     // decode: Base64 → byte[] → hmacShaKeyFor: byte[] → HMAC-Key
    }                                                                                   // privat, weil nur JwtService selbst den Key braucht

    public String generateToken(String mail) {                                          // erzeugt einen signierten JWT für einen User
        Date now = new Date();                                                          // aktuelle Systemzeit für "issuedAt"
        Date expiration = new Date(now.getTime() + expirationMs);                       // now + 24h = Ablaufzeitpunkt für "expiration"
        
        return Jwts.builder()                                                           // fluent Builder starten
                .subject(mail)                                                          // "sub"-Claim setzen = User-Mail (später mit getSubject() lesbar)
                .issuedAt(now)                                                          // "iat"-Claim setzen = Erstellungszeitpunkt
                .expiration(expiration)                                                 // "exp"-Claim setzen = Ablaufzeitpunkt (Parser prüft das automatisch)
                .signWith(getSigningKey())                                              // Header + Payload mit HMAC-SHA256 signieren – ohne das wäre der Token fälschbar
                .compact();                                                             // baut den finalen String: base64(Header).base64(Payload).base64(Signatur)
    }
    
    public String extractMail(String token) {                                           // extrahiert die Mail aus einem Token + prüft Gültigkeit
        try {                                                                           // bei Fehler → catch → return null (signalisiert: Token ungültig)
            Claims claims = Jwts.parser()                                               // ParserBuilder starten
                    .verifyWith(getSigningKey())                                        // gleichen Key wie beim Erstellen – prüft ob Signatur im Token dazu passt
                    .build()                                                            // konfigurierten Parser bauen (thread-safe, kann mehrfach verwendet werden)
                    .parseSignedClaims(token)                                           // 1. Token parsen, 2. Signatur verifizieren, 3. Ablauf prüfen → sonst Exception
                    .getPayload();                                                      // Claims-Objekt aus dem JWS extrahieren (enthält sub, iat, exp)
            
            return claims.getSubject();                                                 // "sub"-Wert zurückgeben = die Mail des Users
            
        } catch (JwtException e) {                                                      // fängt ALLE JWT-Fehler (abgelaufen, Signatur falsch, Token kaputt)
            return null;                                                                // null = Token ist ungültig – Aufrufer (Filter) handelt entsprechend
       }
    }
}
