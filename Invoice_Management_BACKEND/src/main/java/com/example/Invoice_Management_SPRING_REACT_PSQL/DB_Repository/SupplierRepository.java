package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

	Supplier findByName(String name);

}