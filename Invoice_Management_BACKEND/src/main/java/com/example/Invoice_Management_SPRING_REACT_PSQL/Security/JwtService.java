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

    private SecretKey getSigningKey() {                                 
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));      
    }

    public String generateToken(String mail, String role) {             
        Date now = new Date();                                          
        Date expiration = new Date(now.getTime() + expirationMs);       

        return Jwts.builder()                                           
                .subject(mail)                                          
                .claim("role", role)                                    
                .issuedAt(now)                                          
                .expiration(expiration)                                 
                .signWith(getSigningKey())                              
                .compact();                                             
    }

    public String extractMail(String token) {                           
        try {                                                           
            Claims claims = Jwts.parser()                               
                    .verifyWith(getSigningKey())                        
                    .build()                                            
                    .parseSignedClaims(token)                           
                    .getPayload();                                      

            return claims.getSubject();                                 

        } catch (JwtException e) {                                      
            return null;                                                
       }
    }
}
