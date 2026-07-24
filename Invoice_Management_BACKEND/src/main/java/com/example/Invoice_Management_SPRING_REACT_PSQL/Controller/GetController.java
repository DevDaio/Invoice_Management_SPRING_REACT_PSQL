package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;
import org.springframework.web.bind.annotation.RestController;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.*;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.*;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/")
public class GetController {

    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final ArticleRepository articleRepository;
    private final InvoiceRepository invoiceRepository;

    public GetController(UserRepository userRepository, SupplierRepository supplierRepository,
                         ArticleRepository articleRepository, InvoiceRepository invoiceRepository) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.articleRepository = articleRepository;
        this.invoiceRepository = invoiceRepository;
    }


    // ─── GET endpoints ───

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User nicht gefunden");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers(@RequestParam(required = false) String name) {
        if (name != null) return List.of(supplierRepository.findByName(name));
        return supplierRepository.findAll();
    }

    @GetMapping("/articles")
    public List<Article> getArticles(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer articleNumber,
                                     @RequestParam(required = false) String invoiceNumber) {
        if (name != null) return articleRepository.findByName(name);
        if (articleNumber != null) return articleRepository.findByArticleNumber(articleNumber);
        if (invoiceNumber != null) return articleRepository.findByInvoiceNumber(invoiceNumber);
        return articleRepository.findAll();
    }

    @GetMapping("/invoices")
    public List<Invoice> getInvoices(@RequestParam(required = false) String supplierName,
                                     @RequestParam(required = false) String number,
                                     @RequestParam(required = false) LocalDate date,
                                     @RequestParam(required = false) Boolean payed,
                                     @RequestParam(required = false) Integer articleNumber) {
        if (supplierName != null && date != null && payed != null && articleNumber != null)
            return invoiceRepository.findBySupplierNameAndDateAndPayedAndArticleNumber(supplierName, date, payed, articleNumber);
        if (supplierName != null && date != null && payed != null)
            return invoiceRepository.findBySupplierNameAndDateAndPayed(supplierName, date, payed);
        if (supplierName != null && date != null)
            return invoiceRepository.findBySupplierNameAndDate(supplierName, date);
        if (supplierName != null) return invoiceRepository.findBySupplierName(supplierName);
        if (number != null) return invoiceRepository.findByNumber(number);
        if (date != null) return invoiceRepository.findByDate(date);
        if (payed != null) return invoiceRepository.findByPayed(payed);
        if (articleNumber != null) return invoiceRepository.findByArticleNumber(articleNumber);
        return invoiceRepository.findAll();
    }
}
