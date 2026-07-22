package com.example.Invoice_Management_SPRING_REACT_PSQL;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.List;

@SpringBootApplication
public class InvoiceManagementSpringReactPsqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceManagementSpringReactPsqlApplication.class, args);
	}

	@Bean
  	CommandLineRunner runner(UserRepository repository) {
   		return args -> {
 			User user = new User("example@example.org", "holyMoly", "admin");
    		repository.save(user);
    		List<User> users = repository.findAll();
      if (users.isEmpty()) {
          System.out.println("No users found");
      } else {
          users.forEach(System.out::println);
      }
     };
   	};
}
