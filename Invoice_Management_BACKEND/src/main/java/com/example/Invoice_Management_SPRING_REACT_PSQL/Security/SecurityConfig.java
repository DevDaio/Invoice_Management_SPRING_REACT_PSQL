package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;                              
import org.springframework.context.annotation.Bean;                                              
import org.springframework.context.annotation.Configuration;                                      
import org.springframework.security.config.annotation.web.builders.HttpSecurity;                  
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;        
import org.springframework.security.config.http.SessionCreationPolicy;                            
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;                          
import org.springframework.security.crypto.password.PasswordEncoder;                              
import org.springframework.security.web.SecurityFilterChain;                                      
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;      
import org.springframework.web.cors.CorsConfiguration;                                            
import org.springframework.web.cors.CorsConfigurationSource;                                      
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;                              
import org.springframework.http.HttpMethod;                                                       
import java.util.List;                                                                           

@Configuration                                                                                   
@EnableWebSecurity                                                                               
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;                                                    

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {                                          
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {          
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))                    
            .csrf(csrf -> csrf.disable())                                                         
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))                   
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()                                
                .requestMatchers("/newUser").hasRole("ADMIN")                  
                .requestMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")                    
                .requestMatchers(HttpMethod.PUT, "/update/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/update/password").hasRole("ADMIN")
                .anyRequest().authenticated()                                                      
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);          

        return http.build();                                                                      
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {                                           
        String[] allowedOrigins = System.getenv()
            .getOrDefault("APP_CORS_ORIGINS", "http://localhost:5173")
            .split(",");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));             
        config.setAllowedHeaders(List.of("*"));                                                   
        config.setAllowCredentials(true);                                                         

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);                                          
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {                                                    
        return new BCryptPasswordEncoder();                                                       
    }
}
