package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.TaxType;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.UnitType;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Invoice;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Article;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Supplier;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.ArticleRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.SupplierRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.InvoiceRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Security.Crypting;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/update")
public class PutController {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final SupplierRepository supplierRepository;
    private final InvoiceRepository invoiceRepository;

    public PutController(UserRepository userRepository, ArticleRepository articleRepository, SupplierRepository supplierRepository, InvoiceRepository invoiceRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.supplierRepository = supplierRepository;
        this.invoiceRepository = invoiceRepository;
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
        Optional<User> found = userRepository.findByMail(request.getMail());
        if (found.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = found.get();
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
        Optional<User> found = userRepository.findByMail(request.getMail());
        if (found.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User user = found.get();
        user.setPassword(request.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok().body("Password updated successfully");
    }

    public static class OwnPasswordRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    @PutMapping("/own-password")
    public ResponseEntity<?> updateOwnPassword(@RequestBody OwnPasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (!Crypting.checkPassword(user, request.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect");
        }
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password is required");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.ok().body("Password updated successfully");
    }

    public static class ArticleRequest {
        private String articleNumber;
        private String name;
        private double priceNet;
        private String tax;
        private String unit;
        private int quantity;

        public String getArticleNumber() { return articleNumber; }
        public void setArticleNumber(String articleNumber) { this.articleNumber = articleNumber; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPriceNet() { return priceNet; }
        public void setPriceNet(double priceNet) { this.priceNet = priceNet; }
        public String getTax() { return tax; }
        public void setTax(String tax) { this.tax = tax; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class InvoiceUpdateRequest {
        private String number;
        private LocalDate date;
        private String supplierName;
        private Boolean payed;
        private List<ArticleRequest> articles;

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public Boolean getPayed() { return payed; }
        public void setPayed(Boolean payed) { this.payed = payed; }
        public List<ArticleRequest> getArticles() { return articles; }
        public void setArticles(List<ArticleRequest> articles) { this.articles = articles; }
    }

    @PutMapping("/invoice/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable int id, @RequestBody InvoiceUpdateRequest request) {
        Optional<Invoice> found = invoiceRepository.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invoice not found");
        }
        Invoice invoice = found.get();

        if (request.getNumber() != null) invoice.setNumber(request.getNumber());
        if (request.getDate() != null) invoice.setDate(request.getDate());
        if (request.getPayed() != null) invoice.setPayed(request.getPayed());

        if (request.getSupplierName() != null) {
            Supplier supplier = supplierRepository.findByName(request.getSupplierName());
            if (supplier == null) {
                supplier = new Supplier(request.getSupplierName());
                supplierRepository.save(supplier);
            }
            invoice.setSupplier(supplier);
        }

        if (request.getArticles() != null) {
            List<Article> existing = new ArrayList<>(invoice.getArticles());
            for (int i = 0; i < request.getArticles().size(); i++) {
                ArticleRequest ar = request.getArticles().get(i);
                UnitType unit;
                TaxType tax;
                try {
                    unit = UnitType.valueOf(ar.getUnit());
                    tax = TaxType.valueOf(ar.getTax());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ungültige Einheit oder Steuersatz: " + ar.getUnit() + " / " + ar.getTax());
                }
                Article article;
                if (i < existing.size()) {
                    article = existing.get(i);
                    article.setArticleNumber(ar.getArticleNumber());
                    article.setName(ar.getName());
                    article.setPriceNet(ar.getPriceNet());
                    article.setUnit(unit);
                    article.setTax(tax);
                    article.setQuantity(ar.getQuantity());
                } else {
                    article = new Article(
                        ar.getArticleNumber(), ar.getName(), ar.getPriceNet(),
                        unit, tax,
                        ar.getQuantity(), invoice.getSupplier()
                    );
                    article.setInvoice(invoice);
                }
                articleRepository.save(article);
            }
            for (int i = request.getArticles().size(); i < existing.size(); i++) {
                articleRepository.delete(existing.get(i));
            }
        }

        invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoice);
    }
}
