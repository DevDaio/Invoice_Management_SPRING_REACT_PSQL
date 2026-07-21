package com.example.Invoice_Management_SPRING_REACT_PSQL.Classes;

public enum TaxType {
	Tax0(0), Tax7(7), Tax19(19);
	private final int taxRate;

	TaxType(int taxRate) {
		this.taxRate = taxRate;
	}

	public int getTaxRate() {
		return taxRate;
	}
}
