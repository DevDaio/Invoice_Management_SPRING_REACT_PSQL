package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;
import jakarta.persistence.*;

@Entity
@Table(name = "articles")
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "article_number", nullable = false)
	private int articleNumber;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "price_net", nullable = false)
	private double priceNet;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "tax_type", nullable = false)
	private TaxType tax;

	@Enumerated(EnumType.STRING)
	@Column(name = "unit_type", nullable = false)
	private UnitType unit;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;

	@ManyToOne
	@JoinColumn(name = "invoice_id", nullable = false)
	private Invoice invoice;

	public Article(int id, String name, double priceNet, UnitType unit, TaxType tax, int quantity, Supplier supplier) {
		this.articleNumber = id;
		this.name = name;
		this.priceNet = priceNet;
		this.tax = tax;
		this.unit = unit;
		this.quantity = quantity;
		this.supplier = supplier;
	}

	public Long getId() {
		return id;
	}

	public int getArticleNumber() {
		return articleNumber;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public String getName() {
		return this.name;
	}

	public double getPriceGroß() {
		return this.priceNet * (1 + this.tax.getTaxRate() / 100.0);
	}

	public double getPriceNet() {
		return this.priceNet;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public TaxType getTax() {
		return this.tax;
	}

	public UnitType getUnit() {
		return this.unit;
	}

	public void setArticleNumber(int articleNumber) {
		this.articleNumber = articleNumber;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPriceNet(double priceNet) {
		this.priceNet = priceNet;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public void setTax(TaxType tax) {
		this.tax = tax;
	}

	public void setUnit(UnitType unit) {
		this.unit = unit;
	}

}
