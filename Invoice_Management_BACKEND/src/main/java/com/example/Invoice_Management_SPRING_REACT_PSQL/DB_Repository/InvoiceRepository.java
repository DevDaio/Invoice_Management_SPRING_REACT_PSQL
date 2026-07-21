package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Invoice;
import java.util.List;
import java.time.LocalDate;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	List<Invoice> findBySupplierId(int supplierId);

	List<Invoice> findByNumber(String number);
	
	List<Invoice> findByArticles_ArticleNumber(int articleNum);

	List<Invoice> findBySupplierName(String supplierName);
	
	List<Invoice> findByDate(LocalDate date);

	List<Invoice> findBySupplierIdAndDate(int supplierId, LocalDate date);


	
}