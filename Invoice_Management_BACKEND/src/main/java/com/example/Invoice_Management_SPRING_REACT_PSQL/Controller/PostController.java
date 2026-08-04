package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;      
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.*;   
import com.example.Invoice_Management_SPRING_REACT_PSQL.Security.*;        
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.*;         
import java.time.LocalDate;                                                
import java.util.List;                                                     
import java.util.ArrayList;                                                
import java.util.Map;                                                      
import java.util.Optional;                                                 
import org.springframework.http.HttpStatus;                                
import org.springframework.http.ResponseEntity;                            
import org.springframework.web.bind.annotation.*;                          

@RestController                                                           
@RequestMapping("/")                                                      
public class PostController {

    private final UserRepository userRepository;                          
    private final SupplierRepository supplierRepository;                  
    private final ArticleRepository articleRepository;                    
    private final InvoiceRepository invoiceRepository;                    
    private final JwtService jwtService;                                  

    public PostController(UserRepository userRepository, SupplierRepository supplierRepository,   
                          ArticleRepository articleRepository, InvoiceRepository invoiceRepository,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.articleRepository = articleRepository;
        this.invoiceRepository = invoiceRepository;
        this.jwtService = jwtService;
    }

    

    public static class LoginRequest {                                     
        private String mail;                                               
        private String password;                                           

        public LoginRequest(String mail, String password) {                
            this.mail = mail;
            this.password = password;
        }

        public String getMail() { return mail; }                           
        public void setMail(String mail) { this.mail = mail; }             
        public String getPassword() { return password; }                   
        public void setPassword(String password) { this.password = password; } 
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

    public static class InvoiceRequest {                                   
        private String invoiceNumber;                                      
        private LocalDate date;                                            
        private String supplierName;                                       
        private List<ArticleRequest> articles;                             

        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public List<ArticleRequest> getArticles() { return articles; }
        public void setArticles(List<ArticleRequest> articles) { this.articles = articles; }
    }

    

    @PostMapping("/login")                                                 
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {   
        if (request.getMail() == null || request.getPassword() == null) {  
            return ResponseEntity.badRequest()                             
                    .body("Mail und Passwort sind erforderlich");          
        }

        Optional<User> found = userRepository.findByMail(request.getMail());

        if (found.isEmpty() || !Crypting.checkPassword(found.get(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Mail oder Passwort sind inkorrekt");
        }

        User user = found.get();
        return ResponseEntity.ok(
                Map.of("token", jwtService.generateToken(user.getMail(), user.getRole()))
        );
    }

    @PostMapping("/invoice")                                               
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) { 
        if (request.getArticles() == null || request.getArticles().isEmpty()) { 
            return ResponseEntity.badRequest()                             
                    .body("Rechnung muss mindestens einen Artikel enthalten");
        }

        Supplier supplier = supplierRepository.findByName(request.getSupplierName()); 
        if (supplier == null) {                                            
            supplier = new Supplier(request.getSupplierName());            
            supplierRepository.save(supplier);                              
        }

        List<Article> articles = new ArrayList<>();                        
        for (ArticleRequest ar : request.getArticles()) {                  
            UnitType unit;
            TaxType tax;
            try {
                unit = UnitType.valueOf(ar.getUnit());
                tax = TaxType.valueOf(ar.getTax());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body("Ungültige Einheit oder Steuersatz: " + ar.getUnit() + " / " + ar.getTax());
            }
            Article article = new Article(ar.getArticleNumber(), ar.getName(), ar.getPriceNet(),
                unit, tax, ar.getQuantity(), supplier);
            articles.add(article);                                         
        }

        Invoice invoice = new Invoice(request.getInvoiceNumber(), request.getDate(), articles, supplier); 
        invoiceRepository.save(invoice);                                   

        for (Article a : articles) {                                       
            a.setInvoice(invoice);                                         
            articleRepository.save(a);                                     
        }

        return ResponseEntity.status(HttpStatus.CREATED)                   
                .body(invoice);                                            
    }

    public static class UserRequest {                                      
        String mail;                                                       
        String password;                                                   

        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/newUser")                                               
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) { 
        if (userRepository.findByMail(request.getMail()).isPresent()) {    
            return ResponseEntity.status(HttpStatus.CONFLICT)              
                    .body("User existiert bereits");                       
        }
        User user = new User(request.getMail(), request.getPassword(), "USER"); 
        userRepository.save(user);                                         
        return ResponseEntity.status(HttpStatus.CREATED)                   
                .body(user);                                               
    }
}
