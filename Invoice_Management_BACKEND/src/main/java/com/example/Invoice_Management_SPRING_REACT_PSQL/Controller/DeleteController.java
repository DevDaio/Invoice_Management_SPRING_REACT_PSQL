package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/user")
public class DeleteController {
    private final UserRepository userRepository;

    public DeleteController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static class DeleteRequest {
        private String mail;

        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody DeleteRequest request) {
        if (userRepository.findByMail(request.getMail()).isPresent()) {
            userRepository.deleteByMail(request.getMail());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(request.getMail() + " is not existing");
        }
    }
}
