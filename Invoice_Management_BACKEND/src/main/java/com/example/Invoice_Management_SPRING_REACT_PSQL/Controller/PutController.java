package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/update")
public class PutController {
    private final UserRepository userRepository;

    public PutController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static class UserRequest {
        private String mail;
        private String newMail;

        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
        public String getNewMail() { return newMail; }
        public void setNewMail(String newMail) { this.newMail = newMail; }
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest request) {
        if (request.getMail() == null || request.getMail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mail is required");
        }
        User user = userRepository.findByMail(request.getMail()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (request.getNewMail() != null && !request.getNewMail().isEmpty()) {
            user.setMail(request.getNewMail());
        }
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated successfully");
    }

    public static class PasswordRequest {
        private String password;
        private String mail;

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest request) {
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is required");
        }
        User user = userRepository.findByMail(request.getMail()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok().body("Password updated successfully");
    }
}
