package com.example.Invoice_Management_SPRING_REACT_PSQL;                                     
import org.springframework.boot.SpringApplication;                                           
import org.springframework.boot.autoconfigure.SpringBootApplication;                         
import org.springframework.boot.CommandLineRunner;                                          
import org.springframework.context.annotation.Bean;                                          
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;                         
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.*;                      
import java.util.List;                                                                       


@SpringBootApplication                                                                       
public class InvoiceManagementSpringReactPsqlApplication {

	public static void main(String[] args) {                                                
		SpringApplication.run(InvoiceManagementSpringReactPsqlApplication.class, args);    
	}
 
    @Bean                                                                                    
    public CommandLineRunner runner(UserRepository userRepository) {                         
        return args -> {                                                                     
            List<User> users = userRepository.findAll();                                     
            if (users.isEmpty()) {                                                           
                User user = new User("admin@admin.com", "admin", "ADMIN");                  
                userRepository.save(user);                                                   
            }
        };
    }

   	};
