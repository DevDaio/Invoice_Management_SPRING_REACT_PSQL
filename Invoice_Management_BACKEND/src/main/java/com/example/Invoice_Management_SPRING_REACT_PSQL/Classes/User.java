package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;   // Package: Classes – JPA-Entities (Datenbank-Tabellen)
import com.example.Invoice_Management_SPRING_REACT_PSQL.Security.Crypting; // Crypting: eigene Utility für Passwort-Hashing
import jakarta.persistence.*;                                         // JPA: @Entity, @Table, @Id, @GeneratedValue, @Column

@Entity                                                               // Sagt JPA: "Diese Klasse ist eine Datenbank-Tabelle"
@Table(name = "users")                                                // Tabellenname in der DB = "users"
public class User {

    @Id                                                               // = Primary Key in der DB
    @GeneratedValue(strategy = GenerationType.IDENTITY)               // Auto-Increment: DB generiert die ID automatisch
    @Column(name = "id")                                              // Spaltenname in der DB = "id"
    private int id;                                                   // Eindeutige ID (wird von der DB vergeben)

    @Column(name = "mail", nullable = false)                           // Spalte "mail", darf NICHT leer sein (NOT NULL)
    private String mail;                                              // E-Mail-Adresse des Users (auch = Login-Name)

    @Column(name = "password", nullable = false)                       // Spalte "password", darf NICHT leer sein
    private String password;                                          // Gehashter Password-String (NIE Klartext!)

    @Column(name = "role", nullable = false)                           // Spalte "role", darf NICHT leer sein
    private String role;                                              // Rolle: "ADMIN" oder "USER" (bestimmt Berechtigungen)

    protected User() {                                               // Leerer Konstruktor (von JPA benötigt, protected = nicht von außen)
    }

    public User(String mail, String password, String role) {          // Öffentlicher Konstruktor (wird vom Code verwendet)
        this.mail = mail;                                             // Mail setzen
        this.role = role;                                             // Rolle setzen (z.B. "ADMIN" oder "USER")
        setPassword(password);                                        // Passwort setzen (wird dabei gehasht!)
    }

    public int getId() {                                             // Getter für die ID
        return id;
    }

    public boolean isAdmin(){                                         // Prüft ob der User Admin ist
        return role.equals("ADMIN");                                  // true wenn Rolle = "ADMIN"
    }

    public String getRole() {                                        // Getter für die Rolle
        return role;
    }

    public String getMail() {                                        // Getter für die Mail
        return mail;
    }

    public String getPassword() {                                    // Getter für das Passwort
        return password;
    }

    public void setRole(String role) {                               // Setter für die Rolle
        this.role = role;
    }

    public void setId(int id) {                                       // Setter für die ID
        this.id = id;
    }

    public void setMail(String mail) {                               // Setter für die Mail
        this.mail = mail;
    }

    public String setPassword(String password) {                     // Setter für das Passwort (HASHED!)
        this.password = Crypting.encryptPassword(password);           // Passwort wird VOR dem Speichern gehasht (NIEMALS Klartext!)
        return this.password;                                         // Gibt den Hash zurück (nützlich für Logging/Prüfung)
    }

}
