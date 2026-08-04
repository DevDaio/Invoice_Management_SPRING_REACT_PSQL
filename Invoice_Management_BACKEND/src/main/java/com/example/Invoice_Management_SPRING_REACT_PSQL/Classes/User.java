package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;   
import com.example.Invoice_Management_SPRING_REACT_PSQL.Security.Crypting; 
import jakarta.persistence.*;                                         
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity                                                               
@Table(name = "users")                                                
public class User {

    @Id                                                               
    @GeneratedValue(strategy = GenerationType.IDENTITY)               
    @Column(name = "id")                                              
    private int id;                                                   

    @Column(name = "mail", nullable = false)                           
    private String mail;                                              

    @Column(name = "password", nullable = false)                       
    private String password;                                          

    @Column(name = "role", nullable = false)                           
    private String role;                                              

    protected User() {                                               
    }

    public User(String mail, String password, String role) {          
        this.mail = mail;                                             
        this.role = role;                                             
        setPassword(password);                                        
    }

    public int getId() {                                             
        return id;
    }

    public boolean isAdmin(){                                         
        return role.equals("ADMIN");                                  
    }

    public String getRole() {                                        
        return role;
    }

    public String getMail() {                                        
        return mail;
    }
    @JsonIgnore
    public String getPassword() {                                    
        return password;
    }

    public void setRole(String role) {                               
        this.role = role;
    }

    public void setId(int id) {                                       
        this.id = id;
    }

    public void setMail(String mail) {                               
        this.mail = mail;
    }

    public void setPassword(String password) {                           
        this.password = Crypting.encryptPassword(password);                  
    }

}
