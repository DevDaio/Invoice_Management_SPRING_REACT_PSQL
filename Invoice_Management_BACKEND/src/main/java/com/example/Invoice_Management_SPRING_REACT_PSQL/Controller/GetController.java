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


    

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User nicht gefunden"));
    }

    @GetMapping("/suppliers")
    public ResponseEntity<?> getSuppliers(@RequestParam(required = false) String name) {
        if (name != null) {
            Supplier supplier = supplierRepository.findByName(name);
            if (supplier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier nicht gefunden");
            }
            return ResponseEntity.ok(List.of(supplier));
        }
        return ResponseEntity.ok(supplierRepository.findAll());
    }

    @GetMapping("/articles")
    public ResponseEntity<?> getArticles(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer articleNumber,
                                     @RequestParam(required = false) String invoiceNumber) {
        if (name == null && articleNumber == null && invoiceNumber == null) {
            return ResponseEntity.ok(articleRepository.findAll());
        }

        return ResponseEntity.ok(articleRepository.findAll().stream()
                .filter(a -> name == null || name.equals(a.getName()))
                .filter(a -> articleNumber == null || String.valueOf(articleNumber).equals(a.getArticleNumber()))
                .filter(a -> invoiceNumber == null || invoiceNumber.equals(a.getInvoice().getNumber()))
                .toList());
    }

    @GetMapping("/invoices")
    public ResponseEntity<?> getInvoices(@RequestParam(required = false) String supplierName,
                                     @RequestParam(required = false) String number,
                                     @RequestParam(required = false) LocalDate date,
                                     @RequestParam(required = false) Boolean payed,
                                     @RequestParam(required = false) Integer articleNumber) {
        if (number != null) {
            Invoice invoice = invoiceRepository.findByNumber(number);
            if (invoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rechnung nicht gefunden");
            }
            return ResponseEntity.ok(List.of(invoice));
        }

        return ResponseEntity.ok(invoiceRepository.findAll().stream()
                .filter(i -> supplierName == null || supplierName.equals(i.getSupplier().getName()))
                .filter(i -> date == null || date.equals(i.getDate()))
                .filter(i -> payed == null || payed.equals(i.isPayed()))
                .filter(i -> articleNumber == null || i.getArticles().stream()
                        .anyMatch(a -> String.valueOf(articleNumber).equals(a.getArticleNumber())))
                .toList());
    }
}
