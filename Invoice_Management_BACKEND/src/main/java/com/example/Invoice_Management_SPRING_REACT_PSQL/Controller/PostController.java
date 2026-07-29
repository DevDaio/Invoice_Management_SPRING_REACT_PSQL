package com.example.Invoice_Management_SPRING_REACT_PSQL.Controller;      // Package: Controller – hier liegen die REST-Endpoints
import com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository.*;   // Alle Repositories (User, Supplier, Article, Invoice)
import com.example.Invoice_Management_SPRING_REACT_PSQL.Security.*;        // Alle Security-Klassen (JwtService, Crypting, …)
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.*;         // Alle Entities (User, Supplier, Article, Invoice, …)
import java.time.LocalDate;                                                // LocalDate: Datum (für Rechnungsdatum)
import java.util.List;                                                     // List: Interface für Artikel-Liste
import java.util.ArrayList;                                                // ArrayList: konkrete Implementierung für Artikel-Liste
import java.util.Map;                                                      // Map.of() für Token-Rückgabe
import org.springframework.http.HttpStatus;                                // HttpStatus: HTTP-Statuscodes (OK, UNAUTHORIZED, …)
import org.springframework.http.ResponseEntity;                            // ResponseEntity: HTTP-Response mit Status + Body
import org.springframework.web.bind.annotation.*;                          // @RestController, @RequestMapping, @PostMapping, …

@RestController                                                           // Sagt Spring: "Diese Klasse ist ein REST-Controller"
@RequestMapping("/")                                                      // Alle Endpoints starten mit "/" (also /login, /invoice, …)
public class PostController {

    private final UserRepository userRepository;                          // Repository für User-Tabelle (Login, User anlegen)
    private final SupplierRepository supplierRepository;                  // Repository für Supplier-Tabelle
    private final ArticleRepository articleRepository;                    // Repository für Article-Tabelle
    private final InvoiceRepository invoiceRepository;                    // Repository für Invoice-Tabelle
    private final JwtService jwtService;                                  // JwtService: Token generieren (für Login)

    public PostController(UserRepository userRepository, SupplierRepository supplierRepository,   // Konstruktor-Injektion
                          ArticleRepository articleRepository, InvoiceRepository invoiceRepository,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.articleRepository = articleRepository;
        this.invoiceRepository = invoiceRepository;
        this.jwtService = jwtService;
    }

    // ─── DTOs ───                                                        // DTO-Klassen: Daten-Transfer-Objekte (werden aus JSON gemappt)

    public static class LoginRequest {                                     // DTO für POST /login: Mail + Passwort
        private String mail;                                               // User-Mail (aus JSON: "mail": "...")
        private String password;                                           // User-Passwort (aus JSON: "password": "...")

        public LoginRequest(String mail, String password) {                // Konstruktor für JSON-Deserialisierung
            this.mail = mail;
            this.password = password;
        }

        public String getMail() { return mail; }                           // Getter für Spring/Jackson
        public void setMail(String mail) { this.mail = mail; }             // Setter für Spring/Jackson
        public String getPassword() { return password; }                   // Getter für Spring/Jackson
        public void setPassword(String password) { this.password = password; } // Setter für Spring/Jackson
    }

    public static class ArticleRequest {                                   // DTO für POST /invoice: einzelner Artikel im JSON-Array
        private String articleNumber;                                      // Artikelnummer
        private String name;                                               // Artikelname
        private double priceNet;                                           // Nettopreis
        private String tax;                                                // Steuersatz (z.B. "STANDARD", "REDUCED")
        private String unit;                                               // Einheit (z.B. "PIECE", "HOUR")
        private int quantity;                                              // Menge

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

    public static class InvoiceRequest {                                   // DTO für POST /invoice: Rechnung inkl. Artikel-Array
        private String invoiceNumber;                                      // Rechnungsnummer
        private LocalDate date;                                            // Rechnungsdatum
        private String supplierName;                                       // Lieferantenname
        private List<ArticleRequest> articles;                             // Liste der Artikel in dieser Rechnung

        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public List<ArticleRequest> getArticles() { return articles; }
        public void setArticles(List<ArticleRequest> articles) { this.articles = articles; }
    }

    // ─── Endpoints ───                                                   // Ab hier: die REST-Endpoints

    @PostMapping("/login")                                                 // POST http://localhost:8080/login – User einloggen
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {   // @RequestBody: JSON → LoginRequest (Mail + Passwort)
        User user = userRepository.findByMail(request.getMail())           // User in DB suchen anhand der Mail
                .orElse(null);                                             // Nicht gefunden → null (statt Exception)

        if (user == null) {                                                // User existiert nicht in der DB
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)          // HTTP 401: nicht autorisiert
                    .body("Mail oder Passwort sind inkorrekt");            // Fehlermeldung (absichtlich vage: "Mail oder Passwort")
        }

        if (!Crypting.checkPassword(user, request.getPassword())) {       // User existiert, aber Passwort ist falsch
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)          // HTTP 401: nicht autorisiert
                    .body("Mail oder Passwort sind inkorrekt");            // Gleiche Fehlermeldung wie oben (kein Hinweis, welches falsch ist)
        }

        return ResponseEntity.ok(                                          // HTTP 200: Erfolg
                Map.of("token", jwtService.generateToken(user.getMail(), user.getRole()))  // Gibt JSON zurück: {"token": "eyJhbGciOiJIUzI1NiJ9..."}
        );
    }

    @PostMapping("/invoice")                                               // POST http://localhost:8080/invoice – Rechnung anlegen
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) { // JSON → InvoiceRequest (Rechnung + Artikel)
        if (request.getArticles() == null || request.getArticles().isEmpty()) { // Validierung: mindestens ein Artikel nötig
            return ResponseEntity.badRequest()                             // HTTP 400: Bad Request
                    .body("Rechnung muss mindestens einen Artikel enthalten");
        }

        Supplier supplier = supplierRepository.findByName(request.getSupplierName()); // Lieferant in DB suchen
        if (supplier == null) {                                            // Lieferant existiert noch nicht
            supplier = new Supplier(request.getSupplierName());            // Neuen Lieferanten anlegen
            supplierRepository.save(supplier);                              // In DB speichern (damit er eine ID bekommt)
        }

        List<Article> articles = new ArrayList<>();                        // Liste für die Article-Entities (aus den DTOs)
        for (ArticleRequest ar : request.getArticles()) {                  // Jedes ArticleRequest → Article-Entity
            Article article = new Article(ar.getArticleNumber(), ar.getName(), ar.getPriceNet(),
                UnitType.valueOf(ar.getUnit()), TaxType.valueOf(ar.getTax()), ar.getQuantity(), supplier);
            articles.add(article);                                         // Article zur Liste hinzufügen
        }

        Invoice invoice = new Invoice(request.getInvoiceNumber(), request.getDate(), articles, supplier); // Invoice-Entity bauen
        invoiceRepository.save(invoice);                                   // Rechnung in DB speichern

        for (Article a : articles) {                                       // Nach dem Speichern: Bidirektionale Verknüpfung setzen
            a.setInvoice(invoice);                                         // Jeder Artikel bekommt die Invoice-ID
            articleRepository.save(a);                                     // Artikel in DB speichern
        }

        return ResponseEntity.status(HttpStatus.CREATED)                   // HTTP 201: Created
                .body(invoice);                                            // Die gespeicherte Rechnung als JSON zurückgeben
    }

    public static class UserRequest {                                      // DTO für POST /newUser: Mail + Passwort für neuen User
        String mail;                                                       // User-Mail
        String password;                                                   // User-Passwort

        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/newUser")                                               // POST http://localhost:8080/newUser – neuen User anlegen
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) { // JSON → UserRequest (Mail + Passwort)
        if (userRepository.findByMail(request.getMail()).isPresent()) {    // Prüfen: Gibt es schon einen User mit dieser Mail?
            return ResponseEntity.status(HttpStatus.CONFLICT)              // HTTP 409: Conflict
                    .body("User existiert bereits");                       // Fehlermeldung
        }
        User user = new User(request.getMail(), request.getPassword(), "USER"); // Neuen User mit Rolle "USER" anlegen
        userRepository.save(user);                                         // In DB speichern (Passwort wird im Konstruktor gehasht)
        return ResponseEntity.status(HttpStatus.CREATED)                   // HTTP 201: Created
                .body(user);                                               // Den neuen User als JSON zurückgeben
    }
}
