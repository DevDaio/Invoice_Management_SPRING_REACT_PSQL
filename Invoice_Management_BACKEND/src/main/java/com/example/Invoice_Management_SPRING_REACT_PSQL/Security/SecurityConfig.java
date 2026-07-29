package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;                              // Package: Security
import org.springframework.context.annotation.Bean;                                              // @Bean = Methode erzeugt ein Spring-Bean
import org.springframework.context.annotation.Configuration;                                      // @Configuration = diese Klasse definiert Beans
import org.springframework.security.config.annotation.web.builders.HttpSecurity;                  // HttpSecurity: Builder für die Security-Filter-Chain
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;        // @EnableWebSecurity = aktiviert Spring Security
import org.springframework.security.config.http.SessionCreationPolicy;                            // SessionCreationPolicy: STATELESS etc.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;                          // BCryptPasswordEncoder: Standard-Passwort-Hasher
import org.springframework.security.crypto.password.PasswordEncoder;                              // PasswordEncoder: Interface für Passwort-Hashing
import org.springframework.security.web.SecurityFilterChain;                                      // SecurityFilterChain: die konfigurierte Filter-Kette
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;      // Der Standard-Login-Filter (unserer läuft davor)
import org.springframework.web.cors.CorsConfiguration;                                            // CORS-Konfiguration pro Origin
import org.springframework.web.cors.CorsConfigurationSource;                                      // Interface: Quelle für CORS-Konfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;                              // Konkrete Implementierung: registriert Pfad → Config
import org.springframework.http.HttpMethod;                                                       // HttpMethod: DELETE, PUT, GET, …
import java.util.List;                                                                           // List.of() für die CORS-Listen

@Configuration                                                                                   // Sagt Spring: "Das ist eine Konfigurationsklasse"
@EnableWebSecurity                                                                               // Aktiviert Spring Security + überschreibt Defaults
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;                                                    // Unser Token-Filter (wird per Konstruktor injiziert)

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {                                          // Konstruktor-Injektion: Spring übergibt die Bean
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {          // Baut die Security-Filter-Kette
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))                    // CORS aktivieren (React :5173 darf zugreifen)
            .csrf(csrf -> csrf.disable())                                                         // Kein CSRF: JWT im Header statt Session-Cookies
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))                   // Keine HTTP-Session – jeder Request hat eigenen JWT
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()                                // /login darf jeder ohne Token aufrufen
                .requestMatchers("/newUser").hasRole("ADMIN")                  // /newUser nur mit Rolle ROLE_ADMIN (hasRole() ergänzt Prefix automatisch)
                .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")                    // DELETE /user/{mail} nur für Admins
                .requestMatchers(HttpMethod.PUT, "/update/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/update/password").hasRole("ADMIN")
                .anyRequest().authenticated()                                                      // Alle anderen Endpoints brauchen gültigen Login
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);          // Unser Filter läuft VOR dem Standard-Login-Filter

        return http.build();                                                                      // baut die SecurityFilterChain – ohne build() passiert nichts
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {                                           // Definiert: welcher Origin/Zugriff ist erlaubt?
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));                               // React (Vite) läuft auf Port 5173
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));             // Erlaubte HTTP-Methoden
        config.setAllowedHeaders(List.of("*"));                                                   // Alle Header erlaubt (Authorization, Content-Type, …)
        config.setAllowCredentials(true);                                                         // Erlaubt Credentials (für JWT im Authorization-Header)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);                                          // Für ALLE Pfade im Backend gilt diese CORS-Regel
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {                                                    // Wird später für Passwort-Hashing gebraucht
        return new BCryptPasswordEncoder();                                                       // BCrypt = Standard-Algorithmus für Passwörter
    }
}
