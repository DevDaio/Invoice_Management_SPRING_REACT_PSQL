package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;     // Package: Security – hier liegen JWT + Auth-Klassen
import io.jsonwebtoken.Claims;                                           // Claims = Payload eines JWT (enthält sub, iat, exp)
import io.jsonwebtoken.Jwts;                                            // Jwts = zentrale Factory für Builder + Parser
import io.jsonwebtoken.JwtException;                                    // JwtException = Sammel-Exception für alle JWT-Fehler
import io.jsonwebtoken.security.Keys;                                   // Keys = Hilfsklasse für crypto-Operationen (z.B. hmacShaKeyFor)
import io.jsonwebtoken.io.Decoders;                                     // Decoders = Base64 ↔ byte[] Konvertierung
import javax.crypto.SecretKey;                                          // SecretKey = symmetrischer Schlüssel für HMAC-SHA256
import org.springframework.beans.factory.annotation.Value;               // @Value = injiziert Werte aus application.properties
import org.springframework.stereotype.Service;                           // @Service = markiert diese Klasse als Spring-Service-Bean
import java.util.Date;                                                  // Date = Zeitstempel für issuedAt / expiration


@Service                                                                 // Spring: "Erzeuge ein Bean aus dieser Klasse" (für DI)
public class JwtService {                                               // Service: Token bauen (generateToken) + prüfen (extractMail)

    @Value("${app.jwt.secret}")                                         // Value aus application.properties: Base64-Secret
    private String secret;                                              // Wird in getSigningKey() zu HMAC-Schlüssel decodiert

    @Value("${app.jwt.expiration-ms}")                                  // Value aus application.properties: Ablaufzeit in ms
    private long expirationMs;                                          // 86400000 = 24 Stunden

    private SecretKey getSigningKey() {                                 // privat: nur JwtService selbst darf den Key sehen
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));      // Base64 → byte[] → HMAC-SHA-Key (prüft Mindestlänge 256 Bit)
    }

    public String generateToken(String mail) {                          // Erzeugt einen signierten JWT für eine User-Mail
        Date now = new Date();                                          // Aktuelle Systemzeit für "issuedAt"
        Date expiration = new Date(now.getTime() + expirationMs);       // now + 24h = Ablaufzeitpunkt

        return Jwts.builder()                                           // Fluent Builder: baut Schritt für Schritt den Token
                .subject(mail)                                          // "sub"-Claim: die User-Mail (später mit getSubject() lesbar)
                .issuedAt(now)                                          // "iat"-Claim: Erstellungszeitpunkt
                .expiration(expiration)                                 // "exp"-Claim: Ablaufzeitpunkt (Parser prüft automatisch)
                .signWith(getSigningKey())                              // Header + Payload mit HMAC-SHA256 signieren
                .compact();                                             // Erzeugt den finalen String: base64(Header).base64(Payload).base64(Signatur)
    }

    public String extractMail(String token) {                           // Extrahiert Mail aus Token + prüft Gültigkeit (Signatur + Ablauf)
        try {                                                           // Bei Fehler → catch → return null
            Claims claims = Jwts.parser()                               // ParserBuilder starten
                    .verifyWith(getSigningKey())                        // Gleichen Key wie beim Erstellen – prüft die Signatur
                    .build()                                            // Thread-safeen Parser bauen (kann mehrfach verwendet werden)
                    .parseSignedClaims(token)                           // 1. parsen, 2. Signatur prüfen, 3. Ablauf prüfen
                    .getPayload();                                      // Claims-Objekt extrahieren (sub, iat, exp)

            return claims.getSubject();                                 // "sub"-Wert = die Mail des Users

        } catch (JwtException e) {                                      // Fängt ALLE JWT-Fehler (abgelaufen, falsche Signatur, etc.)
            return null;                                                // null = Token ungültig – der Filter handelt entsprechend
       }
    }
}
