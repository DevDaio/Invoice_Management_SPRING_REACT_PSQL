package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	Invoice findByNumber(String number);

}