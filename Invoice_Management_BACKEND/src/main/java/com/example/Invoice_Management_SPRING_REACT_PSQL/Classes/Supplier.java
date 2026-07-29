package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "suppliers")
public class Supplier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "supplier")
	private List<Article> articles = new ArrayList<>();

	@OneToMany(mappedBy = "supplier")
	private List<Invoice> invoices = new ArrayList<>();

	protected Supplier() {
	}

	public Supplier(String name) {
		this.name = name;
	}
	@JsonIgnore
	public List<Article> getArticles() {
		return articles;
	}

	public int getId() {
		return id;
	}
	
	@JsonIgnore
	public List<Invoice> getInvoices() {
		return invoices;
	}

	public String getName() {
		return name;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public void setName(String name) {
		this.name = name;
	}

}
