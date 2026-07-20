package com.example.Invoice_Management_SPRING_REACT_PSQL.Classses;
import java.util.Date;


public class Invoice {
	private int id;
	private String number;
	private Date date;
	private Article[] articles;;
	
	public Invoice(int id, String number, Date date, Article[] articles) {
		this.id = id;
		this.number = number;
		this.date = date;
		this.articles = articles; 
	}
	
	public int getId() {
		return id;
	}
	
	public Article[] getArticles() {
		return articles;
	}
	
	public String getNumber() {
		return number;
	}
	public Date getDate() {
		return date;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}