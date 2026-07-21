package com.example.Invoice_Management_SPRING_REACT_PSQL.DB_Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Invoice_Management_SPRING_REACT_PSQL.Classes.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findByMail(String mail);

}