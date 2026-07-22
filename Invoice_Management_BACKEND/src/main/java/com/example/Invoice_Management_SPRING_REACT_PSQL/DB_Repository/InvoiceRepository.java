package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Invoice;
import java.util.List;
import java.time.LocalDate;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	List<Invoice> findAll();

	List<Invoice> findByNumber(String number);
	
	List<Invoice> findByArticleNumber(int articleNum);

	List<Invoice> findBySupplierName(String supplierName);
	
	List<Invoice> findByDate(LocalDate date);

	List<Invoice> findBySupplierNameAndDate(String supplierName, LocalDate date);

	List<Invoice> findByPayed(boolean payed);

	List<Invoice> findBySupplierNameAndDateAndPayed(String supplierName, LocalDate date, boolean payed);

	List<Invoice> findBySupplierNameAndDateAndPayedAndArticleNumber(String supplierName, LocalDate date, boolean payed, int articleNumber);

	
}