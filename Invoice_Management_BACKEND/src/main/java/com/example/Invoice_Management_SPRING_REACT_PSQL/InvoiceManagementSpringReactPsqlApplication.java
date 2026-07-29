package com.example.Invoice_Management_SPRING_REACT_PSQL;                                     // Unser Package-Name (muss mit Ordnerstruktur übereinstimmen)
import org.springframework.boot.SpringApplication;                                           // Zum Starten der Spring-Anwendung (run-Methode)
import org.springframework.boot.autoconfigure.SpringBootApplication;                         // Kombiniert @Configuration + @EnableAutoConfiguration + @ComponentScan
import org.springframework.boot.CommandLineRunner;                                          // Interface: Code der nach dem Application-Start läuft
import org.springframework.context.annotation.Bean;                                          // Sagt Spring: "Diese Methode erzeugt ein Bean"
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;                         // Unsere User-Entity
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.*;                      // Alle Repository-Interfaces (UserRepository, …)
import java.util.List;                                                                       // Für die findAll()-Rückgabe


@SpringBootApplication                                                                       // Markiert diese Klasse als Spring Boot Einstiegspunkt
public class InvoiceManagementSpringReactPsqlApplication {

	public static void main(String[] args) {                                                // main-Methode – JVM startet hier
		SpringApplication.run(InvoiceManagementSpringReactPsqlApplication.class, args);    // Startet den Spring-Container (Tomcat, Beans, …)
	}
 // ─── startup: admin user anlegen ───
    @Bean                                                                                    // Diese Methode erzeugt ein Bean (CommandLineRunner)
    public CommandLineRunner runner(UserRepository userRepository) {                         // Läuft NACH dem Application-Start – userRepository wird injiziert
        return args -> {                                                                     // Lambda: Code der ausgeführt wird
            List<User> users = userRepository.findAll();                                     // Alle User aus DB holen
            if (users.isEmpty()) {                                                           // Prüfen: Gibt es schon User in der DB?
                User user = new User("admin@admin.com", "admin", "ADMIN");                  // Neuen Admin-User anlegen (Mail, Passwort, Rolle)
                userRepository.save(user);                                                   // In DB speichern
            }
        };
    }

   	};
