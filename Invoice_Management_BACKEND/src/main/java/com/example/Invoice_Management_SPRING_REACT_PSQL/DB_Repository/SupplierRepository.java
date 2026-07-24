package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Supplier;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

	List<Supplier> findAll();
	
	Supplier findByName(String name);

	List<Supplier> findByInvoices_Number(String number);

	List<Supplier> findByArticleNumber(int articleNumber);
	
}