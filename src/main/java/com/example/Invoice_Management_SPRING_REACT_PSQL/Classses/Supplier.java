package com.example.Invoice_Management_SPRING_REACT_PSQL.Classses;

class Supplier {

	private int id;
	private String name;

	public Supplier(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}

}