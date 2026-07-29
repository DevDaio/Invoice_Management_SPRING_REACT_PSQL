package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;           // Package: Security
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository; // Repository: DB-Zugriff auf User-Tabelle
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;          // Entity: User-Klasse (DB-Tabelle "users")
import jakarta.servlet.FilterChain;                                            // FilterChain: Kette von Filtern – doFilter() ruft den nächsten auf
import jakarta.servlet.ServletException;                                       // ServletException: von doFilterInternal() geworfen
import jakarta.servlet.http.HttpServletRequest;                               // HttpServletRequest: der eingehende HTTP-Request
import jakarta.servlet.http.HttpServletResponse;                               // HttpServletResponse: die ausgehende HTTP-Response
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  // Spring Securitys "Authentifizierungs-Ausweis"
import org.springframework.security.core.context.SecurityContextHolder;        // ThreadLocal-Speicher für den Authentication-Status
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Hängt IP/Browser-Details an den Auth-Token
import org.springframework.stereotype.Component;                                // @Component = Spring-Bean (wird automatisch gefunden)
import org.springframework.web.filter.OncePerRequestFilter;                    // Filter-Garantie: nur 1x pro Request (auch bei Forwards)
import java.io.IOException;                                                    // IOException: von doFilterInternal() geworfen
import java.util.List;                                                         // List.of() für die Authorities-Liste
import java.util.Optional;                                                     // Optional<User> = Rückgabe von findByMail (kann leer sein)
import org.springframework.security.core.authority.SimpleGrantedAuthority;      // SimpleGrantedAuthority: einzelne Rolle/Recht (z.B. ROLE_ADMIN)

@Component                                                                      // Spring-Bean: automatisch erkannt und in SecurityConfig injiziert
public class JwtAuthFilter extends OncePerRequestFilter {                       // Einmal pro Request ausgeführt (kein Double-Check)

    private final JwtService jwtService;                                        // JwtService per Konstruktor injiziert
    private final UserRepository userRepository;                                // UserRepository per Konstruktor injiziert

   public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) { // Konstruktor: Spring übergibt beide Beans
        this.jwtService = jwtService;                                           // Feld setzen
        this.userRepository = userRepository;                                   // Feld setzen
    }

    @Override                                                                   // Überschreibt OncePerRequestFilter
    protected void doFilterInternal(                                            // Wichtigste Methode: wird für jeden Request aufgerufen
            HttpServletRequest request,                                         // Der eingehende Request (enthält Header, Body, …)
            HttpServletResponse response,                                       // Die Antwort (wird an Client zurückgeschickt)
            FilterChain filterChain)                                            // Die Filter-Kette – doFilter() ruft den nächsten Filter auf
            throws ServletException, IOException {                              // Beide Exceptions müssen nach oben gereicht werden

        String authHeader = request.getHeader("Authorization");                 // Holt den "Authorization"-Header: "Bearer eyJhbGci..."

        boolean isAuthRequest = authHeader != null                              // Prüft: gibt es den Header überhaupt?
                && authHeader.startsWith("Bearer ");                            // Prüft: fängt er mit "Bearer " an? (Standard-Prefix)

        if (isAuthRequest) {                                                    // Nur wenn Header "Bearer …" vorhanden ist
            String token = authHeader.substring(7);                             // Alles nach "Bearer " = der reine JWT-String
            String mail = jwtService.extractMail(token);                        // Prüft Signatur + Ablauf → gibt Mail zurück (oder null)

            if (mail != null) {                                                 // null = Token ungültig oder abgelaufen → ignorieren
                Optional<User> user = userRepository.findByMail(mail);          // User aus der Datenbank laden (existiert er überhaupt?)

                if (user.isPresent()) {                                         // User gefunden → in SecurityContext einloggen
                    UsernamePasswordAuthenticationToken authToken =              // Spring Securitys "Authentifizierungs-Ausweis"
                            new UsernamePasswordAuthenticationToken(
                                    user.get(),                                  // 1. Principal = die User-Entity (wer ist es?)
                                    null,                                        // 2. Credentials = kein Passwort (JWT hat schon geprüft)
                                    List.of(new SimpleGrantedAuthority("ROLE_" + user.get().getRole())) // 3. Authorities = Rollen
                            );

                    authToken.setDetails(                                       // Hängt IP/Browser an den Token (für Logs, nicht kritisch)
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()                           // **Der entscheidende Moment:**
                            .setAuthentication(authToken);                      // Spring Security "glaubt" jetzt: User ist eingeloggt
                }
            }
        }

        filterChain.doFilter(request, response);                                // OHNE diese Zeile bleibt der Request hängen → Timeout!
    }
}
