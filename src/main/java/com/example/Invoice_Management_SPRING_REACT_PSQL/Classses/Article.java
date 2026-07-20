package com.example.Invoice_Management_SPRING_REACT_PSQL.Classses;;

enum UnitType {
	Pieces, Meter, Box, Pallet, Service, Position
}
enum TaxType{
	Tax0(0), Tax7(7), Tax19(19);
	private final int taxRate;

	TaxType(int taxRate) {
		this.taxRate = taxRate;
	}

	public int getTaxRate() {
		return taxRate;
	}
}

class Article {
	private int id;
	private String name;
	private double priceNet;
	private TaxType tax;
	private UnitType unit;
	private int amount;

	public Article(int id, String name, double priceNet, UnitType unit, TaxType tax, int amount) {
		this.id = id;
		this.name = name;
		this.priceNet = priceNet;
	}
	
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public double getPriceNet() {
		return this.priceNet;
	}
	public double getPriceGroß() {
		return this.priceNet * (1 + this.tax.getTaxRate() / 100.0);
	}
	public UnitType getUnit() {
		return this.unit;
	}
	public TaxType getTax() {
		return this.tax;
	}
	public int getAmount() {
		return this.amount;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPriceNet(double priceNet) {
		this.priceNet = priceNet;
	}
	
}