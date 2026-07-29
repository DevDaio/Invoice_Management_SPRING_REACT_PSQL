package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.InvoiceRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.ArticleRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Article;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Invoice;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/")
public class DeleteController {
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final ArticleRepository articleRepository;

    public DeleteController(UserRepository userRepository, InvoiceRepository invoiceRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.articleRepository = articleRepository;
    }

    public static class DeleteRequest {
        private String mail;

        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteRequest request) {
        if (userRepository.findByMail(request.getMail()).isPresent()) {
            userRepository.deleteByMail(request.getMail());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(request.getMail() + " is not existing");
        }
    }

    @DeleteMapping("/invoice/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable int id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice " + id + " not found");
        }
        for (Article a : articleRepository.findByInvoiceNumber(invoice.getNumber())) {
            articleRepository.delete(a);
        }
        invoiceRepository.delete(invoice);
        return ResponseEntity.noContent().build();
    }
}
