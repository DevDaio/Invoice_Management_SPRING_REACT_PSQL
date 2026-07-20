package com.example.Invoice_Management_SPRING_REACT_PSQL.Classses;

class User{
	private int id;
	private String mail;
	private String password;

	public User(int id, String mail, String password) {
		this.id = id;
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
