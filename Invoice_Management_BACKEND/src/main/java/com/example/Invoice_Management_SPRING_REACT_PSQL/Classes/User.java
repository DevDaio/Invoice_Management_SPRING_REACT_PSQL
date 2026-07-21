package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;
import jakarta.persistence.*;

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

	public User(String mail, String password) {
		this.mail = mail;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
