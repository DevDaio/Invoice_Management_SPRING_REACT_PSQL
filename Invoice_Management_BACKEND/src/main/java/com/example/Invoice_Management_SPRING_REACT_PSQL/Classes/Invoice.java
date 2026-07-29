package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "payed", nullable = false)
	private boolean payed;
	
	@Column(name = "number", nullable = false)
	private String number;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	@OneToMany(mappedBy = "invoice")
	private List<Article> articles = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "supplier", nullable = false, unique = false, updatable = true)
	private Supplier supplier;

	protected Invoice() {
	}

	public Invoice(String number, LocalDate date, List<Article> articles, Supplier supplier) {
		this.number = number;
		this.date = date;
		this.articles = articles;
		this.payed = false;
		this.supplier = supplier;
	}

	public List<Article> getArticles() {
		return new ArrayList<>(articles);
	}

	public LocalDate getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public boolean isPayed() {
		return payed;
	}

	public String getNumber() {
		return number;
	}
	
	public Supplier getSupplier() {
		return supplier;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

}
