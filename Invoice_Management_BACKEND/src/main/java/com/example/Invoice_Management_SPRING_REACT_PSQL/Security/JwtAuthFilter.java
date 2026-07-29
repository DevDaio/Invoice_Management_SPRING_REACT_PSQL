package com.example.Invoice_Management_SPRING_REACT_PSQL.Security;           
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository; 
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;          
import jakarta.servlet.FilterChain;                                            
import jakarta.servlet.ServletException;                                       
import jakarta.servlet.http.HttpServletRequest;                               
import jakarta.servlet.http.HttpServletResponse;                               
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  
import org.springframework.security.core.context.SecurityContextHolder;        
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; 
import org.springframework.stereotype.Component;                                
import org.springframework.web.filter.OncePerRequestFilter;                    
import java.io.IOException;                                                    
import java.util.List;                                                         
import java.util.Optional;                                                     
import org.springframework.security.core.authority.SimpleGrantedAuthority;      

@Component                                                                      
public class JwtAuthFilter extends OncePerRequestFilter {                       

    private final JwtService jwtService;                                        
    private final UserRepository userRepository;                                

   public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) { 
        this.jwtService = jwtService;                                           
        this.userRepository = userRepository;                                   
    }

    @Override                                                                   
    protected void doFilterInternal(                                            
            HttpServletRequest request,                                         
            HttpServletResponse response,                                       
            FilterChain filterChain)                                            
            throws ServletException, IOException {                              

        String authHeader = request.getHeader("Authorization");                 

        boolean isAuthRequest = authHeader != null                              
                && authHeader.startsWith("Bearer ");                            

        if (isAuthRequest) {                                                    
            String token = authHeader.substring(7);                             
            String mail = jwtService.extractMail(token);                        

            if (mail != null) {                                                 
                Optional<User> user = userRepository.findByMail(mail);          

                if (user.isPresent()) {                                         
                    UsernamePasswordAuthenticationToken authToken =              
                            new UsernamePasswordAuthenticationToken(
                                    user.get(),                                  
                                    null,                                        
                                    List.of(new SimpleGrantedAuthority("ROLE_" + user.get().getRole())) 
                            );

                    authToken.setDetails(                                       
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()                           
                            .setAuthentication(authToken);                      
                }
            }
        }

        filterChain.doFilter(request, response);                                
    }
}
