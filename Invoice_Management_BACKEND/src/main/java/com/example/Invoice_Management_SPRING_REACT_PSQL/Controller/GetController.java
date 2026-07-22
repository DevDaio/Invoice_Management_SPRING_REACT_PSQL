package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.*;
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.*;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
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

    // ─── startup: admin user anlegen ───
    @Bean
    public CommandLineRunner runner() {
        return args -> {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                User user = new User("admin@admin.com", "admin", "admin");
                userRepository.save(user);
            }
        };
    }

    // ─── GET endpoints ───

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = false) String mail) {
        if (mail != null) return userRepository.findByMail(mail).stream().toList();
        return userRepository.findAll();
    }

    @GetMapping("/suppliers")
    public List<Supplier> getSuppliers(@RequestParam(required = false) String name) {
        if (name != null) return supplierRepository.findByName(name);
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
